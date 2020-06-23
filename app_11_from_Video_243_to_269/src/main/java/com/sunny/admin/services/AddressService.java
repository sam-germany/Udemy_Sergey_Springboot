package com.sunny.admin.services;

import java.util.List;

import com.sunny.admin.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto>  getAllAddresses(String userid);
    
     AddressDto getOneAddress(String addressid);
}
