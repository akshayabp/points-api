package com.fetchrewards.pointsapi.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fetchrewards.pointsapi.utils.CustomLocalDateDeserializer;

public class Transaction {
	
	private String payer;
	
	private long points;
	
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDateTime timestamp;
	
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Transaction [payer=" + payer + ", points=" + points + ", timestamp=" + timestamp + "]";
	}
	
}
