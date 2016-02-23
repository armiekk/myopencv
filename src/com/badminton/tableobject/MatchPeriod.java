package com.badminton.tableobject;

import javafx.scene.image.ImageView;

public class MatchPeriod {
	private double time;
	private ImageView event;
	public MatchPeriod(double time, ImageView event) {
		super();
		this.time = time;
		this.event = event;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public ImageView getEvent() {
		return event;
	}
	public void setEvent(ImageView event) {
		this.event = event;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MatchPeriod [time=" + time + ", event=" + event + "]";
	}
	
	
}
