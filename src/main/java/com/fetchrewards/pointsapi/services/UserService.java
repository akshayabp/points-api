package com.fetchrewards.pointsapi.services;

import com.fetchrewards.pointsapi.model.CreateUserDTO;
import com.fetchrewards.pointsapi.model.User;

public interface UserService {
	
	CreateUserDTO create(CreateUserDTO user);
	
	void delete(String userId);
	
	User get(String userId);

}
