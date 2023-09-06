package com.likebook.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.likebook.entity.Notification;
import com.likebook.entity.User;
import com.likebook.requestdto.ChangePasswordRequest;
import com.likebook.requestdto.EmailVerification;
import com.likebook.requestdto.LoginRequest;
import com.likebook.requestdto.LoginWithCodeRequest;
import com.likebook.requestdto.OtpRequest;
import com.likebook.requestdto.SignupRequest;
import com.likebook.requestdto.UpdateDetailsRequest;
import com.likebook.responsedto.LoginResponse;
import com.likebook.responsedto.Response;
import com.likebook.responsedto.UserResponse;
import com.likebook.service.UserService;
import com.likebook.util.JWTTokenUtil;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController implements Serializable {
	
	private static final long serialVersionUID = -285890316463215729L;
	
	private final UserService userService;
	

    @PostMapping("/send-otp")
    Response<String> sendotp(@Valid @RequestBody OtpRequest request) throws UnsupportedEncodingException, MessagingException {
    	return userService.sendOTPEmail(request);
    }	
    @PostMapping("/verify-email")
    Response<String> verifyEmail( @Valid @RequestBody EmailVerification request){
    	return userService.emailVerification(request);
    	
    }
    @PostMapping("/create-user")
    Response<UserResponse> createUser(@Valid @RequestBody SignupRequest request){
    	return userService.createUser(request);
    }
    @PostMapping("/login")
    Response<LoginResponse> login( @Valid @RequestBody LoginRequest request,@RequestHeader("IpAddress")String IpAddress){
    	return userService.login(request, IpAddress);
    }
    @PostMapping("/login-code")
    Response<LoginResponse> loginWithCode(@Valid @RequestBody LoginWithCodeRequest request,@RequestHeader("IpAddress")String IpAddress){
    	return userService.loginWithCode(request, IpAddress);
    }
    @GetMapping("/user-details")
    Response<UserResponse> userDetails(@RequestHeader(HttpHeaders.AUTHORIZATION)String token ){
    	return userService.userDetails(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @GetMapping("/user-details/{Email}")
    Response<UserResponse> userDetails(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE)String language,@PathVariable(name="Email")String email ){
    	return userService.userDetailsByEmail(email,JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @PostMapping("/forgot-password/{Email}")
    Response<String> forgetPassword(@PathVariable(name="Email") String email) throws UnsupportedEncodingException, MessagingException{
    	return userService.forgotPassword(email);
    }
    @PutMapping("/change-password")
    Response<String> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,	@RequestBody ChangePasswordRequest request){
    	return userService.changePassword(JWTTokenUtil.getUserIdFromToken(token.substring(7)), request);
    }
    @DeleteMapping("/{Email}")
    Response<String> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@PathVariable(name="Email")String email){
    	return userService.deleteUser(JWTTokenUtil.getUserIdFromToken(token.substring(7)), email);
    }
    @PostMapping("/upload-pic")
    public Response<String> uploadImage(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@RequestParam("productImage") MultipartFile file) throws IOException{
    	  return userService.uploadProfilePic(JWTTokenUtil.getUserIdFromToken(token.substring(7)),file);
    	
    }
    @GetMapping("/download-Pic")
    public Response<byte[]> downloadImage(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
    	return userService.downloadProfilePic(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @DeleteMapping("/profile")
    public Response<String> deleteImage(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
    	return userService.deleteProfilePic(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @PutMapping("/update")
    public Response<String> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION)String token, @RequestBody UpdateDetailsRequest request){
    	return userService.updateDetails(JWTTokenUtil.getUserIdFromToken(token.substring(7)),request);
    }
    @GetMapping("")
    public Response<List<User>> getAllUsersList(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
    	return userService.getAllUsers(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @GetMapping("/{Name}")
    public Response<List<User>> searchByName(@PathVariable(name="Name")String name){
    	return userService.serachUserByName(name);
    }
    @GetMapping("/notification")
    public Response<List<Notification>> getAllNotification(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
    	return userService.getAllNotification(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
    }
    @DeleteMapping("/notification/{notificationId}")
    public Response<String> deleteNotification(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@PathVariable("notificationId")String notificationId){
    	return userService.deleteNotification(JWTTokenUtil.getUserIdFromToken(token.substring(7)),notificationId);
    }
}
