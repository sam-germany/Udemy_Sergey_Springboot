package com.sunny.admin.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunny.admin.exceptions.UserServiceException;
import com.sunny.admin.model.request.UserDetailsRequestModel;
import com.sunny.admin.model.response.AddressesRest;
import com.sunny.admin.model.response.ErrorMessagesEnum;
import com.sunny.admin.model.response.OperationStatusModel2;
import com.sunny.admin.model.response.RequestOperationStatus;
import com.sunny.admin.model.response.UserRest;
import com.sunny.admin.services.AddressService;
import com.sunny.admin.services.UserService2;
import com.sunny.admin.shared.dto.AddressDto;
import com.sunny.admin.shared.dto.UserDto;

@RestController
@RequestMapping("users")
//@CrossOrigin(origins="*")
public class UserController {

	
	@Autowired
	UserService2 uService;
	
	
	@Autowired
	AddressService addService;
	
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE ,
			                                 MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		
		UserRest  returnValue = new UserRest();
		
		UserDto userDto = uService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		
		
		return returnValue;
	}
	
	@PostMapping( consumes = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE },
			      produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	public UserRest  createUser(@RequestBody  UserDetailsRequestModel userDetails ) throws Exception {
		
		UserRest  returnValue = new UserRest();
		
        if ( userDetails.getFirstname().isEmpty())
	                        throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage());
		 
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
 
		UserDto createdUser = uService.createUser(userDto);
        returnValue =  modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE },
		                        produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id , @RequestBody  UserDetailsRequestModel userDetails) {
		
		UserRest  returnValue = new UserRest();
	
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser = uService.updateUser(id , userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}
	
//	@Secured("ROLE_ADMIN")
	@DeleteMapping(path = "/{id}" , produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel2 deleteUser(@PathVariable String id ) {
		
		OperationStatusModel2 returnValue = new OperationStatusModel2();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		
		uService.deleteUser(id);
		
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		
		return returnValue;
	}
	
	
	@GetMapping( produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE } )
	public List<UserRest> getUsers(@RequestParam(value = "page" , defaultValue = "0") int page ,
			                       @RequestParam(value = "limit" , defaultValue = "25") int limit){
		
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = uService.getUsers(page, limit);
		
		for(UserDto userDto : users) {
			UserRest userModel  = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		
		return returnValue;
		
	}
	//                               {id}
	//http://localhost:9999/users/fjkldlolsesa/addresses
	    @GetMapping(path = "/{id}/addresses", 
	    		produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	  public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id) {
	    	
	    	List<AddressesRest> returnValue = new ArrayList<>();
	    	
	    	List<AddressDto> addressesDto = addService. getAllAddresses(id);

	    	if(addressesDto != null && !addressesDto.isEmpty())
	    	{
	    		java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {}.getType();   //<--  to understand this line
                returnValue = new ModelMapper().map(addressesDto, listType);          // we need see video 105 Get List of Addresses Web Service Endpoint
	    	                                                                         // the above line we have created only for the ModelMapper() to     
	                                                                                 // we create an anonymous subclass of the TypeToken class passing
	                                                                              //  List<AddressesRest> as a type parameter . This allows the resulting 
	                                                                            // ListType   to contain the captured type information , so that ModelMapper
	                                                                              // knows we mean   .map( from List<AddressDto>    to  List<AddressesRest> )
	                                                                             // in small Broun book i wrote a explanation page also
	                     for(AddressesRest addressRest : returnValue) {
	                    	  Link selfLink  = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
	                    			                                                     .getUserAddress(id, addressRest.getAddressid()))
	                    			                            .withSelfRel();
	                    	  addressRest.add(selfLink);
	                                                                  }
	    	            }// above if() block ends hier 
	        
	    
	    	      Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
	    	    		                           .slash(id)
	    	                                       .withRel("user");
	    	      Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
	    	    		                                                    .getUserAddresses(id))
	    	    		                                                    .withSelfRel();
	    	                    
              	    return CollectionModel.of(returnValue, userLink, selfLink); 
     	    }
	    
	    
	  //http://localhost:9999/users/{userId}/addresses/{addressId}
	    @GetMapping(path = "/{userid}/addresses/{addressid}", 
	    	             	produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	 public EntityModel<AddressesRest>  getUserAddress(@PathVariable String userid , @PathVariable String addressid) {
	    	
	    	AddressDto addressDto = addService.getOneAddress(addressid);
	      
	    	ModelMapper modelMapper = new ModelMapper();
	    	AddressesRest returnValue = modelMapper.map(addressDto, AddressesRest.class);
	    	
	    	Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userid).withRel("user");
	    	Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
	    			                                                           .getUserAddresses(userid))
	    			                                .withRel("addresses");
	    	
	    	Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userid, addressid))

	    			                                        .withSelfRel();
	    	
	    	return EntityModel.of(returnValue, Arrays.asList(userLink , userAddressesLink, selfLink));
	    	
	    }
	
	    
	    @GetMapping(path = "/email-verification" ,
	    		             produces = { MediaType.APPLICATION_XML_VALUE , MediaType.APPLICATION_JSON_VALUE })
	    public OperationStatusModel2 verifyEmailToken(@RequestParam(value = "token") String token) {
	    	
	    	OperationStatusModel2 returnValue = new OperationStatusModel2();
	    	returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
	    	
	    	boolean isVerified = uService.verifyEmailToken(token);
	    	
	    	
	    	if(isVerified)
	    	{
	    		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
	    	}else {
	    		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
	    	}
	    	
	    	
	    	return returnValue;
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
