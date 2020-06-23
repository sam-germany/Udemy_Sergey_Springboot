package com.sunny.admin.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sunny.admin.entity.UserEntity;
import com.sunny.admin.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
    UserRepository uRepo;
	
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetVerifiedUsers() {
		 Pageable pageableRequest = PageRequest.of(0, 2);
          Page<UserEntity> pages =   uRepo.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(pages);
	}

}
