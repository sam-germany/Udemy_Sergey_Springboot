package com.sunny.admin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.sunny.admin.entity.UserEntity;
import com.sunny.admin.repositories.UserRepository;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter1 extends BasicAuthenticationFilter {

	private final UserRepository uRepo;   //as this is a Filter class so we can not use @Component/@Service hier so this is the reason 
	                                      //  we are using Constructor injection for UserRepository, as it is defined in the constructor
	                                     // so we can use it in this class it is injected. 
	
	public AuthorizationFilter1(AuthenticationManager authenticationManager ,  UserRepository uRepo) {
		super(authenticationManager);
		this.uRepo = uRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			                              HttpServletResponse res, 
			                                       FilterChain chain)  throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication22(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);

	}

	
	private UsernamePasswordAuthenticationToken getAuthentication22(HttpServletRequest request) {

		String token = request.getHeader(SecurityConstants.HEADER_STRING);

		if (token != null) {
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

			String user = Jwts.parser()
					          .setSigningKey(SecurityConstants.getTokenSecret())
					          .parseClaimsJws(token)
					          .getBody()
					          .getSubject();

			if (user != null) {
				UserEntity userEntity = uRepo.findByEmail(user);
				UserPrincipal userPrincipal = new UserPrincipal(userEntity);

				return new UsernamePasswordAuthenticationToken(user, null, userPrincipal.getAuthorities());
				
			//	return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}                                 //NOTE:  hier it reutrn after the autorization on the user  means
			                                   // in our case the User email address. the password will be removed
                                               // after the authorization , remember the diagram of the  OAuth 2.0 Shiva
			return null;                        //                                               udemy course.

		}
		return null;

	}

}
