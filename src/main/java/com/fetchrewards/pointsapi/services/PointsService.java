package com.fetchrewards.pointsapi.services;

import java.util.List;
import java.util.Map;

import com.fetchrewards.pointsapi.model.PointsTracker;
import com.fetchrewards.pointsapi.model.Transaction;

public interface PointsService {

	Transaction addPoints(String userId, Transaction transaction);
	
	Map<String, Long> getPayerBalances(String userId);
	
	List<PointsTracker> spendPoints(String userId, long totalPointsToSpend);
	
}
