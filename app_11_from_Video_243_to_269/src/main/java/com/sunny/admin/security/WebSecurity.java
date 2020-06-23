package com.sunny.admin.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sunny.admin.repositories.UserRepository;
import com.sunny.admin.services.UserService2;

@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurity  extends WebSecurityConfigurerAdapter{
	
         private final UserService2 userService2;
         private final BCryptPasswordEncoder bCryptPasswordEncoder;
         private final UserRepository uRepo;
	


		public WebSecurity(UserService2 userService2, BCryptPasswordEncoder bCryptPasswordEncoder , UserRepository uRepo) {
			this.userService2 = userService2;
			this.bCryptPasswordEncoder = bCryptPasswordEncoder;
			this.uRepo = uRepo;
		}


		@Override
		protected void configure(HttpSecurity http) throws Exception {
                   
			http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL).permitAll()
                .antMatchers(HttpMethod.DELETE , "/users/**").hasRole("ADMIN")                    // <--
                .antMatchers("/v2/api-docs" , "/configuration/**" , "/swagger*/**" , "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter1())
                .addFilter(new AuthorizationFilter1(authenticationManager() , uRepo))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
	     /* .antMatchers(HttpMethod.DELETE , "/users/**").hasAuthority("DELETE_AUTHORITY")	
	         to pass multiple authorities                .hasAnyAuthority("DELETE_AUTHORITY", "DELETE_ALL_AUTHORITY")         
	   
	    	                                             .hasRole("ADMIN")
	    	 to pass multiple authorities                .hasAnyRole("ADMIN" , "SUPER_ADMIN")
	    		                                            
		*/	
		} 


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                 auth.userDetailsService(userService2).passwordEncoder(bCryptPasswordEncoder);
		}
		
		public AuthenticationFilter1 getAuthenticationFilter1() throws Exception {
			final AuthenticationFilter1 filter = new AuthenticationFilter1(authenticationManager());
			filter.setFilterProcessesUrl("/users/login");
			return filter;
		}
		
		
}

/*
 (1)
 @Override
		protected void configure(HttpSecurity http) throws Exception {
                                              http.csrf()
                                                  .disable()
                                                  .authorizeRequests()
                                                  .antMatchers(HttpMethod.POST, "/users").permitAll()
                                                  .anyRequest().authenticated();
		                                  }
  
  .csfr()  means     Cross-Site Request Forgery   that  spring security disable all unautorized calls come
           to the application, only those calls that defined hier with   "permit()"  are alloweed
           
           this above is very simple example how we allow   anyMatchers  uri  request coming from postman
           .antMatchers(HttpMethod.POST, "/users").permitAll()        with "/users"   and POST   
           
           this is alloweed as we put a   create()  method , so for create all the calls are   PUBLIC  and for
           rest all we need a authentication.
 ----------------------------------------------------------------------------------------------------------------
 (2) 
 
 @Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                 auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
		}
         
     this we use that the user put the password in the login page , then the  password will be come on the way     
     with the request so their on the way it should be come in the form of BCryptPasswordEncoder  format
 */


