package com.fetchrewards.pointsapi.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fetchrewards.pointsapi.model.CreateUserDTO;
import com.fetchrewards.pointsapi.model.PointsTracker;
import com.fetchrewards.pointsapi.model.Transaction;
import com.fetchrewards.pointsapi.model.User;

public class PointsServiceImplTest {

	@Test
	public void test1() {
		
		String userId="1";
		
		CreateUserDTO user = new CreateUserDTO();
		user.setUserId(userId);
		
		UserService userService = new UserServiceImpl();
		userService.create(user);
		
		PointsServiceImpl serviceImpl = new PointsServiceImpl();
		serviceImpl.setUserService(userService);
		
		Transaction transaction1 = createTransaction("DANNON", 1000, "2020-11-02T14:00:00Z");		
		serviceImpl.addPoints(userId, transaction1);
		
		Transaction transaction2 = createTransaction("UNILEVER", 200, "2020-10-31T11:00:00Z");		
		serviceImpl.addPoints(userId, transaction2);
		
		Transaction transaction3 = createTransaction("DANNON", -200, "2020-10-31T15:00:00Z");		
		serviceImpl.addPoints(userId, transaction3);
		
		Transaction transaction4 = createTransaction("MILLER COORS", 10000, "2020-11-01T14:00:00Z");		
		serviceImpl.addPoints(userId, transaction4);
		
		Transaction transaction5 = createTransaction("DANNON", 300, "2020-10-31T10:00:00Z");		
		serviceImpl.addPoints(userId, transaction5);
		
		List<PointsTracker> pointsTracker = serviceImpl.spendPoints(userId, 5000);
		
		System.out.println(pointsTracker);
		
//		 { "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
//		 { "payer": "UNILEVER", "points": 200, "timestamp": "2020-10-31T11:00:00Z" }
//		 { "payer": "DANNON", "points": -200, "timestamp": "2020-10-31T15:00:00Z" }
//		 { "payer": "MILLER COORS", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" }
//		 { "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }
		
	}
	
	@Test
	public void test2() {
		
		String userId="2";
		
		CreateUserDTO createUserDTO = new CreateUserDTO();
		createUserDTO.setUserId(userId);
		
		UserService userService = new UserServiceImpl();
		userService.create(createUserDTO);
		
		PointsServiceImpl serviceImpl = new PointsServiceImpl();
		serviceImpl.setUserService(userService);
		
		Transaction transaction1 = createTransaction("DANNON", 200, "2020-11-02T14:01:00Z");		
		serviceImpl.addPoints(userId, transaction1);		
						
		Transaction transaction5 = createTransaction("DANNON", 1000, "2020-11-02T14:03:00Z");		
		serviceImpl.addPoints(userId, transaction5);
		
		Transaction transaction3 = createTransaction("DANNON", -300, "2020-11-02T14:02:00Z");		
		serviceImpl.addPoints(userId, transaction3);
		
		List<PointsTracker> pointsTracker = serviceImpl.spendPoints(userId, 400);
		
		System.out.println(pointsTracker);
		
		User user=userService.get(userId);
		Map<String, Long> payerPointsMap = user.getPayerPointsMap();
		System.out.println(payerPointsMap);
		
//		 { "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
//		 { "payer": "UNILEVER", "points": 200, "timestamp": "2020-10-31T11:00:00Z" }
//		 { "payer": "DANNON", "points": -200, "timestamp": "2020-10-31T15:00:00Z" }
//		 { "payer": "MILLER COORS", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" }
//		 { "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }
		
	}
	
	private Transaction createTransaction(String payer, long points, String iso8601DateTime) {
		Transaction transaction1 = new Transaction();
		transaction1.setPayer(payer);
		transaction1.setPoints(points);
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).optionalStart()
				.appendOffset("+HH", "Z")
			    .toFormatter();
		
		LocalDateTime timestamp = LocalDateTime.parse(iso8601DateTime,formatter);
		transaction1.setTimestamp(timestamp);	
		
		return transaction1;
	}
	
}
