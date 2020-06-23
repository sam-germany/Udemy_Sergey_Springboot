package com.sunny.admin.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.sunny.admin.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {

	private final Random RANDOM  = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	
	public String generatedUserId(int length) {
		return generateRendomString(length);
	}
	
	public String generatedAddressId(int length) {
		return generateRendomString(length);
	}
	
	private String generateRendomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		
		for( int i = 0; i< length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return  new String(returnValue);
	}
	
	public static boolean hasTokenExpired(String token) {
		Claims claims = Jwts.parser()
				            .setSigningKey(SecurityConstants.getTokenSecret())
				            .parseClaimsJws( token )
				            .getBody();
		
		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();                     
		
		return tokenExpirationDate.before(todayDate);  // this is simple but little trickey, if the expirey DATE is still not
	}                                                  // their and need to come in FUTURE then this method return TRUE
	                                                   // but if the expirey DATE was in past after checking Todays DATE        
	                                                   // then this method return FALSE
	
	public String generateEmailVerificationToken(String userid) {
		String token =  Jwts.builder()
	                        .setSubject(userid)
	                        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
	                        .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
	                        .compact();
		
		return token;
	}           
}
