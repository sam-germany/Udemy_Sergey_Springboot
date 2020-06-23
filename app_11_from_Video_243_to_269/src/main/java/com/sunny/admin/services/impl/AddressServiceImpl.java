package com.sunny.admin.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunny.admin.entity.AddressEntity;
import com.sunny.admin.entity.UserEntity;
import com.sunny.admin.repositories.AddressRepository;
import com.sunny.admin.repositories.UserRepository;
import com.sunny.admin.services.AddressService;
import com.sunny.admin.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	UserRepository uRepo;
	
	@Autowired
	AddressRepository addRepo;
	
	
	@Override
	public List<AddressDto> getAllAddresses(String userid) {

		List<AddressDto> returnValue  = new ArrayList<>();
        ModelMapper modelMapper  = new ModelMapper();
		
		                                                                    // read in Broun copy i wrote their
		UserEntity userDetails = uRepo.findByUserid(userid);  //<-- in video he put the refrence name as   userEntity  but internally
		if(userDetails == null) return returnValue;           // it is userDetails   as we deifed this userDetails in the 
		                                                                          // address class
		Iterable<AddressEntity> addresses = addRepo.findAllByUserdetails(userDetails);  // 
		
		for(AddressEntity addressEntity : addresses)
		{
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}


	@Override
	public AddressDto getOneAddress(String addressid) {
           
		AddressDto returnValue  = null;
        
		AddressEntity addressEntity = addRepo.findByAddressid(addressid);
		
		if(addressEntity != null)
		{
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

	
	 
	   
	
	
}
