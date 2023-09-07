package com.likebook.service;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.likebook.requestdto.OtpRequest;
import com.likebook.requestdto.SignupRequest;
import com.likebook.requestdto.UpdateDetailsRequest;
import com.likebook.entity.Notification;
import com.likebook.entity.User;
import com.likebook.requestdto.ChangePasswordRequest;
import com.likebook.requestdto.EmailVerification;
import com.likebook.requestdto.LoginRequest;
import com.likebook.requestdto.LoginWithCodeRequest;
import com.likebook.responsedto.LoginResponse;
import com.likebook.responsedto.Response;
import com.likebook.responsedto.UserResponse;

import jakarta.mail.MessagingException;

public interface UserService extends Serializable {

	  Response<String> sendOTPEmail(OtpRequest request) throws UnsupportedEncodingException, MessagingException;
	  Response<String> emailVerification(EmailVerification request);
	  Response<UserResponse> createUser(SignupRequest request);
	  Response<LoginResponse> login(LoginRequest request, String IpAddress);
	  Response<UserResponse> userDetails(String userId);
	  Response<UserResponse> userDetailsByEmail(String email,String userId);
	  Response<String> forgotPassword(String email) throws UnsupportedEncodingException, MessagingException;
	  Response<LoginResponse> loginWithCode(LoginWithCodeRequest request,String IpAddress);
	  Response<String> changePassword(String userId, ChangePasswordRequest request);
	  Response<String> deleteUser(String email, String userId);
	  Response<String> uploadProfilePic(String userId,MultipartFile file) throws IOException;
	  Response<byte[]> downloadProfilePic(String userId);
	  Response<String> deleteProfilePic(String userId);
	  Response<String> updateDetails(String userId,UpdateDetailsRequest request);
	  Response<List<User>> getAllUsers(String userId);
	  Response<List<User>> serachUserByName(String name);	
	  Response<List<Notification>> getAllNotification(String userId);
	  Response<String> deleteNotification(String userId,String notificationId);
	  Response<User> findUserById(String userId);
}
