package com.badminton.application;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.badminton.tableobject.MatchPeriod;
import com.badminton.tableobject.Player;

import services.PointManage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.BmtDetail;
import models.BmtEvent;

public class FXController{
	@FXML
	private Button startBtn;
	@FXML
	private ImageView currentFrame;
	@FXML
	private Button loadBtn;
	@FXML
	private Button addPlayerBtn;
	/* ************************* */
	/* input form */
	@FXML 
	private TextField playerInput;
	@FXML 
	private DatePicker matchDayInput;
	@FXML 
	private TextField tournamentInput;
	@FXML 
	private TextField matchTitleInput;
	@FXML 
	private TextField stadiumInput;
	
	/* ************************* */
	/*player table*/
	@FXML
	private TableView<Player> playerTable;
	@FXML
	private TableColumn<Player, String> playerCol;
	@FXML
	private TableColumn<Player, String> matchDayCol;
	@FXML
	private TableColumn<Player, String> tournamentCol;
	@FXML
	private TableColumn<Player, String> matchTitleCol;
	@FXML
	private TableColumn<Player, String> stadiumCol;
	
	/* ************************* */
	/*match period detail*/
	@FXML
	private TableView<MatchPeriod> matchPeriodTable;
	@FXML
	private TableColumn<MatchPeriod, Double> timeCol;
	@FXML
	private TableColumn<MatchPeriod, Image> eventCol;
	/* ************************* */
	/*data bind tableView */
	private ObservableList<Player> playerData = FXCollections.observableArrayList();
	
	private ObservableList<MatchPeriod> matchPeriodData = FXCollections.observableArrayList();
	/* ************************* */
	/*static variable */
	private ScheduledExecutorService timer;
	
	private VideoCapture capture = new VideoCapture();
	
	private boolean cameraActive = false;
	
	final FileChooser fileChooser = new FileChooser();
	
	private Queue<List<Point>> queue2 = new LinkedList<>();
	/* ************************* */
	
	@FXML
	private void initialize(){
		/*binding player tableColumn*/
		playerCol.setCellValueFactory(new PropertyValueFactory<Player, String>("playerName"));
		matchDayCol.setCellValueFactory(new PropertyValueFactory<Player, String>("date"));
		tournamentCol.setCellValueFactory(new PropertyValueFactory<Player, String>("tournament"));
		matchTitleCol.setCellValueFactory(new PropertyValueFactory<Player, String>("match"));
		stadiumCol.setCellValueFactory(new PropertyValueFactory<Player, String>("stadium"));
		playerTable.setItems(playerData);
		/* ********************************* */
		/*binding matchPeriod tableColumn*/
		timeCol.setCellValueFactory(new PropertyValueFactory<MatchPeriod, Double>("time"));
		eventCol.setCellValueFactory(new PropertyValueFactory<MatchPeriod, Image>("event"));
		matchPeriodTable.setItems(matchPeriodData);
		/* ********************************* */
	}
	
	@FXML
	protected void startCamera(ActionEvent event){
		if(!this.cameraActive){
			if(this.capture.isOpened()){
				
				this.cameraActive = true;
				
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run() {
						
						Image imageToShow = grabFrame();
						currentFrame.setImage(imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 60, TimeUnit.MILLISECONDS);
				
				this.startBtn.setText("Pause Video");
			} else{
				System.err.println("Impossible to open the camera connection...");
			}
			
		} else{
			this.cameraActive = false;
			
			this.startBtn.setText("Play Video");
			
			
			try {
				
				this.timer.shutdown();
				this.timer.awaitTermination(60, TimeUnit.MILLISECONDS);
				
			} catch (InterruptedException  e) {
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			
			//this.capture.release();
			
			//this.currentFrame.setImage(null);
		}
	}
	@FXML
	protected void browVideo(ActionEvent event) {
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			this.capture.open(file.getAbsolutePath());
		}
	}
	@FXML
	protected void winSmashPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/smash.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void winDropPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/drop.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void winOutPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/out.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void winNetPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/net.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void loseSmashPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/smash.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void loseDropPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/drop.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void loseOutPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/out.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void loseNetPoint(ActionEvent event){
		double time = this.capture.get(Videoio.CAP_PROP_POS_MSEC)/1000;
		ImageView image = new ImageView();
		image.setFitWidth(120);
		image.setFitHeight(35);
		image.setImage(new Image("./resources/net.png"));
		MatchPeriod matchPeriod = new MatchPeriod(time, image);
		matchPeriodData.add(matchPeriod);
	}
	@FXML
	protected void addPlayer(ActionEvent event){
		Player add = new Player(playerInput.getText(), matchDayInput.getValue().toString(), tournamentInput.getText(), 
				matchTitleInput.getText(), stadiumInput.getText());
		System.out.println(add);
		playerData.add(add);
	}
	
	//สร้าง frame ที่แสดง video
	private Image grabFrame(){
		Image imageToShow = null;
		Mat frame = new Mat();
		
		if(this.capture.isOpened()){
			try {
				this.capture.read(frame);
				if(!frame.empty()){
					Mat blurredImage = new Mat();
					Mat hsvImage = new Mat();
					Mat mask = new Mat();
					Mat morphOutput = new Mat();
					
					Imgproc.blur(frame, blurredImage, new Size(10, 10));
					Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
					
					//set color
					Scalar minValue = new Scalar(162, 161, 86);
					
					Scalar maxValue = new Scalar(180, 255, 255);
					
					Core.inRange(hsvImage, minValue, maxValue, mask);
					
					Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
					Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
					
					Imgproc.erode(mask, morphOutput, erodeElement);
					Imgproc.erode(mask, morphOutput, erodeElement);
					
					Imgproc.dilate(mask, morphOutput, dilateElement);
					Imgproc.dilate(mask, morphOutput, dilateElement);	
					
					frame = this.findAndDrawContour(morphOutput, frame);
					
					imageToShow = mat2Image(frame);
					
					
				}else{
					this.timer.shutdown();
					this.timer.awaitTermination(60, TimeUnit.MILLISECONDS);
				}
				
			} catch (Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		
		
		return imageToShow;
	}
//�?ปลง Mat เป็น Image
	private Image mat2Image(Mat frame){
		
		MatOfByte buffer = new MatOfByte();
		
		Imgcodecs.imencode(".png", frame, buffer);
		
		
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	private List<Point> findCentroid(MatOfPoint2f[] contours){
		
		List<Point> centroid = new ArrayList<>();
		
		for (int i = 0; i < contours.length; i++) {
			Moments m = Imgproc.moments(contours[i]);
			
			int cx = (int) (m.m10/m.m00);
			int cy = (int) (m.m01/m.m00);
			
			centroid.add(new Point(cx, cy));
		}
		
		return centroid;
	}
	
	private Mat drawPath(MatOfPoint2f[] contours, Mat frame){
		List<Point> centroid = findCentroid(contours);
		this.queue2.add(centroid);
		Point last = null; int count = 1;

		
		for (List<Point> current : this.queue2) {
			if(count % 30 == 0){ this.queue2.remove(); }
			for (int i = 0; i < current.size(); i++) {
				if(last != null && current.get(i) != null){
					Imgproc.line(frame, current.get(i), last, new Scalar(255, 0, 0),2);
				}
				last = current.get(i);
			}
			count++;
		}
		
		return frame;
	}
	
	private Mat findAndDrawContour(Mat maskedImage, Mat frame){
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		
		
		
		Imgproc.findContours(maskedImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		
		if(contours.size() > 0 && contours.size() <= 1){
			Rect[] boundRect = new Rect[contours.size()];
			MatOfPoint2f[] contours_poly = new MatOfPoint2f[contours.size()];
			Point[] center = new Point[contours.size()];
			float[] radius = new float[contours.size()];
			
			
			
			
			for (int i = 0; i < contours.size(); i++) {
				contours_poly[i] = new MatOfPoint2f();
				center[i] = new Point();
				MatOfPoint2f curve = new MatOfPoint2f(contours.get(i).toArray());
				boundRect[i] = Imgproc.boundingRect(new MatOfPoint(curve.toArray()));
				Imgproc.approxPolyDP(curve, contours_poly[i], 3.0, true);
				Imgproc.minEnclosingCircle(contours_poly[i], center[i], radius);
				
			}
			
			frame = drawPath(contours_poly, frame);
			
			
			for (int i = 0; i < contours_poly.length; i++)
			{
				//if(objectBoundingRectangle.area()>500)
				//Imgproc.drawContours(frame, contours, i,  new Scalar(0,255,0));
				//Imgproc.circle(frame, center[i], (int)radius[i], new Scalar(0,255,0), 2);
				//Imgproc.line(frame, center[i], center[i],  new Scalar(255,0,0));
				if(boundRect[i].area()>500)
				Imgproc.rectangle(frame, boundRect[i].tl(), boundRect[i].br(), new Scalar(0,255,0));
			}
		}else{
			this.queue2 = new LinkedList<>();
		}

		return frame;
	}
}
