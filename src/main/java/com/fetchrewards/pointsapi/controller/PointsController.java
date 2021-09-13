package com.fetchrewards.pointsapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.pointsapi.model.PayerDetailsDTO;
import com.fetchrewards.pointsapi.model.PointsTracker;
import com.fetchrewards.pointsapi.model.Transaction;
import com.fetchrewards.pointsapi.model.User;
import com.fetchrewards.pointsapi.services.PointsService;
import com.fetchrewards.pointsapi.services.PointsServiceImpl;
import com.fetchrewards.pointsapi.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@Api(value="user", description="Operations pertaining to points in points-api")
public class PointsController {

	@Autowired
	PointsService pointsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/points/userId/{userId}",  method=RequestMethod.POST, consumes="application/json")
	@ApiOperation(value = "Adds points to user by payer", response = Iterable.class)
	public Transaction addPoints(@PathVariable("userId") String userId, @RequestBody Transaction transaction){			
		pointsService.addPoints(userId, transaction);
		return transaction;
	}
	
	@RequestMapping(value="/points/userId/{userId}/pointsToSpend/{points}",  method=RequestMethod.POST)
	@ApiOperation(value = "Spends points for user", response = Iterable.class)
	public List<PointsTracker> spendPoints(@PathVariable("userId") String userId, @PathVariable("points") long points){			
		List<PointsTracker> pointsTracker=pointsService.spendPoints(userId, points);
		return pointsTracker;
	}
	
	@RequestMapping(value="/payers/userId/{userId}",  method=RequestMethod.GET)
	@ApiOperation(value = "Gets points for all payers for user", response = Iterable.class)
	public PayerDetailsDTO getAllPayerDetails(@PathVariable("userId") String userId) {
		PayerDetailsDTO payerDetails = new PayerDetailsDTO();
		
		Map<String, Long> payerPointsMap=pointsService.getPayerBalances(userId);
		payerDetails.setPayerPointsMap(payerPointsMap);
		payerDetails.setUserId(userId);	
		
		return payerDetails;
	}
	
}
