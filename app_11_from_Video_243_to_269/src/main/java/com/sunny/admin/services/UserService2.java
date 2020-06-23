package com.sunny.admin.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sunny.admin.shared.dto.UserDto;

public interface UserService2  extends UserDetailsService{

	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);
	
	UserDto updateUser(String userid, UserDto userDto);
	
	void deleteUser(String userid);
	
	List<UserDto> getUsers(int page, int limit);
	
	boolean verifyEmailToken(String token);
}
