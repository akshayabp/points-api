package com.fetchrewards.pointsapi.model;

import java.util.Map;

public class PayerDetailsDTO {

	private String userId;
	
	private Map<String, Long> payerPointsMap;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, Long> getPayerPointsMap() {
		return payerPointsMap;
	}

	public void setPayerPointsMap(Map<String, Long> payerPointsMap) {
		this.payerPointsMap = payerPointsMap;
	}
	
}
