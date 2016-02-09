package application;



import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXController {
	@FXML
	private Button start_btn;
	@FXML
	private ImageView currentFrame;
	
	private ScheduledExecutorService timer;
	
	private VideoCapture capture = new VideoCapture();
	
	private boolean cameraActive = false;
	
	@FXML
	protected void startCamera(ActionEvent event){
		if(!this.cameraActive){
			
			this.capture.open("./opencv_cut.mp4");
			
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
				
				this.start_btn.setText("Stop Camera");
			} else{
				System.err.println("Impossible to open the camera connection...");
			}
			
		} else{
			this.cameraActive = false;
			
			this.start_btn.setText("Start Button");
			
			try {
				this.timer.shutdown();
				this.timer.awaitTermination(60, TimeUnit.MILLISECONDS);
				
			} catch (InterruptedException  e) {
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			
			this.capture.release();
			
			this.currentFrame.setImage(null);
		}
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
					
					Imgproc.blur(frame, blurredImage, new Size(7, 7));
					Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
					
					//set color
					Scalar minValue = new Scalar(0, 0, 0);
					
					Scalar maxValue = new Scalar(180, 68, 66);
					
					Core.inRange(hsvImage, minValue, maxValue, mask);
					
					Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
					Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
					
					Imgproc.erode(mask, morphOutput, erodeElement);
					Imgproc.erode(mask, morphOutput, erodeElement);
					
					Imgproc.dilate(mask, morphOutput, dilateElement);
					Imgproc.dilate(mask, morphOutput, dilateElement);
					
					frame = this.findAndDrawContour(morphOutput, frame);
					
					imageToShow = mat2Image(frame);
					
				}
				
			} catch (Exception e) {
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		
		
		return imageToShow;
	}
	
	//แปลง Mat เป็น Image
	private Image mat2Image(Mat frame){
		
		MatOfByte buffer = new MatOfByte();
		
		Imgcodecs.imencode(".png", frame, buffer);
		
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	private Mat findAndDrawContour(Mat maskedImage, Mat frame){
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		
		Imgproc.findContours(maskedImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		
		if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
		{
			// for each contour, display it in blue
			for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
			{
				Imgproc.drawContours(frame, contours, idx, new Scalar(250, 0, 0));
			}
		}
		
		return frame;
	}
}
