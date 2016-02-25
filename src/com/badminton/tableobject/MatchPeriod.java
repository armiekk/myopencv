package com.badminton.tableobject;

import javafx.scene.image.ImageView;

public class MatchPeriod {
	private int time;
	private int winPoint;
	private int losePoint;
	private ImageView event;
	private int set;
	
	public MatchPeriod() {
		super();
	}
	public MatchPeriod(int time, int winPoint, int losePoint, ImageView event, int set) {
		super();
		this.time = time;
		this.winPoint = winPoint;
		this.losePoint = losePoint;
		this.event = event;
		this.set = set;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getWinPoint() {
		return winPoint;
	}
	public void setWinPoint(int winPoint) {
		this.winPoint = winPoint;
	}
	public int getLosePoint() {
		return losePoint;
	}
	public void setLosePoint(int losePoint) {
		this.losePoint = losePoint;
	}
	public ImageView getEvent() {
		return event;
	}
	public void setEvent(ImageView event) {
		this.event = event;
	}
	public int getSet() {
		return set;
	}
	public void setSet(int set) {
		this.set = set;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + losePoint;
		result = prime * result + set;
		result = prime * result + time;
		result = prime * result + winPoint;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchPeriod other = (MatchPeriod) obj;
		if (losePoint != other.losePoint)
			return false;
		if (set != other.set)
			return false;
		if (time != other.time)
			return false;
		if (winPoint != other.winPoint)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MatchPeriod [time=" + time + ", winPoint=" + winPoint + ", losePoint=" + losePoint + ", event=" + event
				+ ", set=" + set + "]";
	}
	
	
	
}
