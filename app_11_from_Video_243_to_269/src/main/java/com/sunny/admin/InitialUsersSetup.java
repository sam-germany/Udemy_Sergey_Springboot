package com.sunny.admin;

import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.sunny.admin.entity.AuthorityEntity;
import com.sunny.admin.entity.RoleEntity;
import com.sunny.admin.entity.UserEntity;
import com.sunny.admin.repositories.AuthorityRepository;
import com.sunny.admin.repositories.RoleRepository;
import com.sunny.admin.repositories.UserRepository;
import com.sunny.admin.shared.Utils;

@Component
public class InitialUsersSetup {
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	AuthorityRepository authRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		System.out.println("just for showing that Application is ready ---");
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
        RoleEntity roleUser  =  createRole("ROLE_USER" , Arrays.asList(readAuthority, writeAuthority));
		RoleEntity roleAdmin = createRole("ROLE_ADMIN",  Arrays.asList(readAuthority, writeAuthority,deleteAuthority ));
		
        if(roleAdmin == null) return;		
		
        UserEntity adminUser= new UserEntity();
        adminUser.setFirstname("bunny");
        adminUser.setLastname("singh");
        adminUser.setEmail("ttt@gmail.com");
        adminUser.setIsactive(true);
        adminUser.setEmailverificationstatus(true);
        adminUser.setUserid(utils.generatedUserId(30));
        adminUser.setEncryptedpassword(bCryptPasswordEncoder.encode("123456"));
		adminUser.setRoles(Arrays.asList(roleAdmin));
		
		uRepo.save(adminUser);
		
		
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) {
		
		AuthorityEntity authority = authRepo.findByName(name);
		
		if(authority == null ) {
			authority = new AuthorityEntity(name);
			authRepo.save(authority);                    // in if() statement we do not need to put any  "return" keyword
		}                                               // as "authority" will be updated   and at end we are returning
		                                                //  "return  authority;"
		return authority;
	}
	
	
	@Transactional
	private RoleEntity createRole( String name, Collection<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepo.findByName(name);
		if(role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepo.save(role);
		}
		
		return role;
		
	}

}
