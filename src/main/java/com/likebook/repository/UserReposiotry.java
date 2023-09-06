package com.likebook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.likebook.entity.User;
import com.likebook.responsedto.UserResponse;

public interface UserReposiotry extends JpaRepository<User, String>{
	
	Optional<User> findByEmail(String email); 
	
	Optional<User> findByIdAndEmail(String userId, String email);
	
	@Query("SELECT new com.likebook.responsedto.UserResponse(id,email,firstName,lastName,phoneNumber,dateOfBirth,password,country,accountType.name,occupation.name,active,isEmailVerified ) FROM User WHERE id= ?1 ")
	UserResponse getUserDetails(String id);
	
	@Query("SELECT new com.likebook.responsedto.UserResponse(id,email,firstName,lastName,phoneNumber,dateOfBirth,password,country,accountType.name,occupation.name,active,isEmailVerified ) FROM User WHERE id= ?1 ")
	Optional<UserResponse> getUserDetailsById(String id);
	
	@Query("SELECT new com.likebook.responsedto.UserResponse(id,email,firstName,lastName,phoneNumber,dateOfBirth,password,country,accountType.name,occupation.name,active,isEmailVerified ) FROM User WHERE id= ?1 and email= ?2 ")
	Optional<UserResponse> getUserDetailsByEmailAndId(String id,String email);
	
	boolean existsByEmail(String email);
	
	@Query("SELECT u FROM User u ")
    List<User> getAllUsers();
	@Query("SELECT u FROM User u WHERE u.firstName = ?1")
    List<User> searchUserByName(String name);
}
