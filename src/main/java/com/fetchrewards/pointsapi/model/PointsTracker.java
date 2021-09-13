package com.fetchrewards.pointsapi.model;

public class PointsTracker {

	private String payer;
	
	private long points;
	
	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}
	
	@Override
	public String toString() {
		return "PointsTracker [payer=" + payer + ", points=" + points + "]";
	}
	
}
