package com.fetchrewards.pointsapi.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fetchrewards.pointsapi.constants.ErrorCodes;
import com.fetchrewards.pointsapi.model.CreateUserDTO;
import com.fetchrewards.pointsapi.model.User;

@Service
public class UserServiceImpl implements UserService{

	private static final Map<String, User> users = new HashMap<>();
	
	@Override
	public CreateUserDTO create(CreateUserDTO user) {
		User userCopy = null;
		
		String userId = user.getUserId();
		
		if(users.containsKey(userId)) {
			throw new RuntimeException(ErrorCodes.USER_ALREADY_EXIST);
		}
		
		userCopy = new User();
		userCopy.setUserId(userId);
		users.put(userId, userCopy);	
		
		return user;
	}
	
	@Override
	public void delete(String userId) {
		
		if(users.containsKey(userId)) {
			users.remove(userId);
		}else {
			throw new RuntimeException(ErrorCodes.USER_DOES_NOT_EXIST);
		}
	}
	
	
	@Override
	public User get(String userId) {
		return users.get(userId);
	}

	
	
}
