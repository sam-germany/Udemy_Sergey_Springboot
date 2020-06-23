package com.sunny.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sunny.admin.entity.AuthorityEntity;
import com.sunny.admin.entity.RoleEntity;
import com.sunny.admin.entity.UserEntity;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -2798378039431510898L;

	
	UserEntity userEntity;
	
	public UserPrincipal(UserEntity userEntity) {
         this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> grantedAll = new ArrayList<>();
	
		// getting user Roles and Authorities 
		Collection<RoleEntity> roles1 = userEntity.getRoles();
		List<AuthorityEntity> authorityEntities = new ArrayList<>();
		
		if(roles1 == null) return grantedAll;     // if roles== null      then we are returning a empty LIST<>
		
		roles1.forEach((role2) -> {
		                         	grantedAll.add(new SimpleGrantedAuthority(role2.getName()));
			                        authorityEntities.addAll(role2.getAuthorities());
		                           });
		
		authorityEntities.forEach((x ) -> {           // hier we are retriving and adding single authority one by one
			                                grantedAll.add(new SimpleGrantedAuthority(x.getName()));                           		
	                                      	});
		
		return grantedAll;
	}

	@Override
	public String getPassword() {
		return this.userEntity.getEncryptedpassword();
	}

	@Override
	public String getUsername() {
		return this.userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.userEntity.getEmailverificationstatus();
	}

}
