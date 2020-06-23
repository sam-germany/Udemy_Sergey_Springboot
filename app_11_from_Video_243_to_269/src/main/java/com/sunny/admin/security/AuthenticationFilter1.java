package com.sunny.admin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.admin.SpringApplicationContext;
import com.sunny.admin.model.request.UserLoginRequestModel;
import com.sunny.admin.services.UserService2;
import com.sunny.admin.shared.dto.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class AuthenticationFilter1 extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter1(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpReq, 
			                                    HttpServletResponse httpRes) throws AuthenticationException {

		try {
			UserLoginRequestModel creds = new ObjectMapper()
					                                     .readValue(httpReq.getInputStream(), UserLoginRequestModel.class);
		
		   return authenticationManager.authenticate(  new UsernamePasswordAuthenticationToken(creds.getEmail(),
		    		                                  		                                   creds.getPassword(),
		    		                                   		                                   new ArrayList<>()
		    		                                   		                                   )
		    		                                 );
		    		                                 
		}catch (IOException e) {
                 throw new RuntimeException(e);
                      		}
                                                      	 }



	@Override
	protected void successfulAuthentication(HttpServletRequest httpReq,
			                               HttpServletResponse httpRes,
			                                FilterChain chain, 
			                                Authentication authResult) throws IOException, ServletException {

		String userName = ((UserPrincipal) authResult.getPrincipal())
				                                            .getUsername();
     
		
		// String tokenSecret = new SecurityConstants().getTockenSecret();
		
		//https://github.com/sunnykeila/jjwt_-JSON-WEB-TOKEN-documentation-   read documentation to understand JWT
		// i write some notes in the folder i created for this course
		
		String token = Jwts.builder()
				           .setSubject(userName)
				           .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				           .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				           .compact();
		
	  UserService2   userService  = (UserService2)SpringApplicationContext.getBean("userService2Impl");
	  UserDto userDto = userService.getUser(userName);
		
		
		
		 httpRes.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		httpRes.addHeader("UserId", userDto.getUserid());
	}










}
