package com.fetchrewards.pointsapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class User {

	private String userId;
	
	private PriorityQueue<Transaction> transactions;
	
	private Map<String, Long> payerPointsMap;
	
	private long totalPoints;

	public User() {
		super();
		
		this.totalPoints=0l;
		this.payerPointsMap = new HashMap<>();
		this.transactions = new PriorityQueue<Transaction>((t1, t2)->{
			return t1.getTimestamp().compareTo(t2.getTimestamp());
		});		
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public PriorityQueue<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(PriorityQueue<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Map<String, Long> getPayerPointsMap() {
		return payerPointsMap;
	}

	public void setPayerPointsMap(Map<String, Long> payerPointsMap) {
		this.payerPointsMap = payerPointsMap;
	}

	public long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(long totalPoints) {
		this.totalPoints = totalPoints;
	}	
	
	
		
}
