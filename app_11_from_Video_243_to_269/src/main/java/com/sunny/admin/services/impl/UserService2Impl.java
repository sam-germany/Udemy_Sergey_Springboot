package com.sunny.admin.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunny.admin.entity.UserEntity;
import com.sunny.admin.exceptions.UserServiceException;
import com.sunny.admin.model.response.ErrorMessagesEnum;
import com.sunny.admin.repositories.UserRepository;
import com.sunny.admin.security.UserPrincipal;
import com.sunny.admin.services.UserService2;
import com.sunny.admin.shared.AmazonSES;
import com.sunny.admin.shared.Utils;
import com.sunny.admin.shared.dto.AddressDto;
import com.sunny.admin.shared.dto.UserDto;

@Service
public class UserService2Impl implements UserService2 {

	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
                   	if(uRepo.findByEmail(userDto.getEmail())  != null)
        		                 throw new RuntimeException("Record already exists");
		
        	
        	      for(int i=0; i < userDto.getAddresses().size(); i++) {
        	    	       AddressDto address = userDto.getAddresses().get(i);
        	    	       address.setUserdetails(userDto);                    //<-- only this line is little confusing, the main point 
        	    	       address.setAddressid(utils.generatedAddressId(30));     // is that we have to set in AddressDto that for which
        	    	       userDto.getAddresses().set(i, address);                  // User exactelly we are setting this address,
        	             }                                                        // as we want to search also by Address that for which      
        	                                                                      // user this Address belongs to, so this is the reason
		            ModelMapper modelMapper = new ModelMapper();
		            UserEntity  userEntity = modelMapper.map(userDto, UserEntity.class);
		            
		            
	            	String publicUserId = utils.generatedUserId(30);
             		userEntity.setUserid(publicUserId);
	            	userEntity.setEncryptedpassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		            userEntity.setEmailverificationtoken(utils.generateEmailVerificationToken(publicUserId));
		            userEntity.setEmailverificationstatus(false);
		            
	            	
            		UserEntity  storedUserDetails = uRepo.save(userEntity);
              		UserDto  returnValue = modelMapper.map(storedUserDetails, UserDto.class);
		
              		new AmazonSES().verifyEmail(returnValue);
              		
	      	return returnValue;
  	     }
	

	       @Override
	       public UserDto getUser(String email) {
	    	   
               UserEntity userEntity = uRepo.findByEmail(email);
              
               if(userEntity == null) throw new UsernameNotFoundException(email);
               
               UserDto returnValue = new UserDto();
               BeanUtils.copyProperties(userEntity, returnValue);
               
	    	   return returnValue;
	           }


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity =   uRepo.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);

		  return new UserPrincipal(userEntity);
		
//	return new User(userEntity.getEmail(), userEntity.getEncryptedpassword(), userEntity.getEmailverificationstatus(),
//				true, true,true  , new ArrayList<>());
	//	return new User(userEntity.getEmail() , userEntity.getEncryptedpassword() ,new  ArrayList <>());
	}


     	@Override
	    public UserDto getUserByUserId(String userId) {

     		      UserDto returnValue = new UserDto();
     		      UserEntity userEntity = uRepo.findByUserid(userId);
     		      
     		     if(userEntity == null) throw new UsernameNotFoundException("user with if " + userId + " not found");
     		      
     		     BeanUtils.copyProperties(userEntity, returnValue);
     		     
     		
     		return returnValue;
	        }


		@Override
		public UserDto updateUser(String userid, UserDto userDto) {
			  
			  UserDto returnValue = new UserDto();
		      UserEntity userEntity = uRepo.findByUserid(userid);
		      
		      if(userEntity == null ) throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());
			
		      
		      userEntity.setFirstname(userDto.getFirstname());
		      userEntity.setLastname(userDto.getLastname());
		      
		      UserEntity updatedUserDetails  =  uRepo.save(userEntity);
		      BeanUtils.copyProperties(updatedUserDetails, returnValue);
		       
		       
			return returnValue;
		}


		@Override
		public void deleteUser(String userid) {

		     UserEntity userEntity = uRepo.findByUserid(userid);
		     
		     if(userEntity == null ) throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());
		     
		     uRepo.delete(userEntity);
			
		}


		@Override
		public List<UserDto> getUsers(int page, int limit) {
            
			List<UserDto> returnValue = new ArrayList<>();
		
			if (page> 0) page -=1;    // means  if (page> 0) page  = page -1 ;  
			
			Pageable pageableRequest = PageRequest.of(page, limit);
			
			Page<UserEntity> usersPage = uRepo.findAll(pageableRequest);
			List<UserEntity> users  = usersPage.getContent();
			
			for(UserEntity userEntity : users) {
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(userEntity, userDto);
                returnValue.add(userDto);
			}
			
			
			return returnValue;
		}


		@Override
		public boolean verifyEmailToken(String token) {
			boolean returnValue = false;
			
			UserEntity userEntity = uRepo.findUserByEmailverificationtoken(token);
                                                                          // little trickey logic
			if(userEntity != null) {                                     // if TOKEN is expired then it return TRUE so  
				boolean hastokenExpires = Utils.hasTokenExpired(token);   // !TRUE  = false  then if() statement will not be executed
				if(!hastokenExpires) {                                    
					userEntity.setEmailverificationtoken(null);           // But if the TOCKEN is not expired then it return  
					userEntity.setEmailverificationstatus(Boolean.TRUE);  // !FALSE  = TRUE  then the if() statement will be
					uRepo.save(userEntity);                               // executed
					returnValue = true;
				}
			}
			
			return false;
		}

     




}
