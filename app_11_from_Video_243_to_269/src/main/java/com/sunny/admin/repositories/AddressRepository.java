package com.sunny.admin.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sunny.admin.entity.AddressEntity;
import com.sunny.admin.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserdetails(UserEntity userEntity);
	
	AddressEntity findByAddressid(String addressid);
}
