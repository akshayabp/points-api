package com.fetchrewards.pointsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.pointsapi.model.CreateUserDTO;
import com.fetchrewards.pointsapi.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@Api(value="user", description="Operations pertaining to user in points-api")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	@RequestMapping(value="/user",  method=RequestMethod.POST, consumes="application/json")
	@ApiOperation(value = "Creates new user", response = Iterable.class)
	public CreateUserDTO createUser(@RequestBody CreateUserDTO createUserDTO){			
		CreateUserDTO createdUser =userService.create(createUserDTO);
		return createdUser;
	}
	
	@RequestMapping(value="/user/{userId}",  method=RequestMethod.DELETE)
	@ApiOperation(value = "Deletes existing user", response = Iterable.class)
	public void deleteUser(@PathVariable("userId") String id){			
		userService.delete(id);		
	}

}
