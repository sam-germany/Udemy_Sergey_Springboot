package com.sunny.admin.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sunny.admin.entity.UserEntity;

@Repository
public interface UserRepository  extends PagingAndSortingRepository<UserEntity, Long>{ 
	
	UserEntity findByEmail(String email);
	UserEntity findByUserid(String userId);
	UserEntity findUserByEmailverificationtoken(String token);
	
	@Query(value="select * from users u where u.EMAIL_VERIFICATION_STATUS ='false'",
             countQuery = "select count(*) form users u where u.EMAIL_VERIFICATION_STATUS = 'false'",			  
			 nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

/*	
	//                                  where u.firstname = ?1 and u.last_name=?2    <-- if 2 argument then use like this
	@Query(value="select * from users u where u.firstname = ?1" , nativeQuery = true)
	List<UserEntity> findUserByFirstName(String firstname);
	
	
	@Query(value="select * from users u where u.firstname = :f2" , nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("f2")String f1);
	
	
	@Query(value="select * from users u where u.firstname LIKE :%f2 " , nativeQuery = true) // video 219
	List<UserEntity> findUserByKeyword(@Param("f2")String keyword);
	
	@Query(value="select * from users u where u.firstname LIKE :%f2 or lastname LIKE %:keyword" , nativeQuery = true) // video 219
	List<UserEntity> findUserByKeyword2(@Param("f2")String keyword);
	
	
	
	// video 220
	@Query(value="select u.firstname , u.lastname  from users u where u.firstname LIKE :%f2 or lastname LIKE %:keyword" , nativeQuery = true) // video 219
	List<Object[]> findUserFirstNameAndLastName(@Param("f2")String keyword);
	
	@Modifying
	@Transactional 
	@Query("UPDATE UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId = :userId")
	void updateUserEntityEmailVerificationStatus(	@Param("emailVerificationStatus") boolean emailVerificationStatus,
	                                                @Param("userId") String userId);
*/	
}
