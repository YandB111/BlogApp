package com.yb.blogApp.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yb.blogApp.dto.UserSignUpDTO;
import com.yb.blogApp.entity.UserSignUp;

@Repository
public interface UserSignUpServiceRepo extends JpaRepository<UserSignUp, Long>{

	UserSignUp findByPhoneNumber(String phoneNumber);

	void save(UserSignUpDTO user);

	
	List<UserSignUp> findByVerifiedFalseAndCreatedAtBefore(Date oneDayAgo);

	UserSignUp findByEmail(String email);

	

}
