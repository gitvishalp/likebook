package com.likebook.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likebook.entity.Friend;
import com.likebook.entity.User;
import com.likebook.repository.AccountTypeRepository;
import com.likebook.repository.EmailCodeRepository;
import com.likebook.repository.FriendRepository;
import com.likebook.repository.OccupationRepository;
import com.likebook.repository.OtpRepository;
import com.likebook.repository.ProfilePictureRepository;
import com.likebook.repository.UserReposiotry;
import com.likebook.responsedto.Response;
import com.likebook.service.FriendService;
import com.likebook.util.GenerateOtp;
import com.likebook.util.JWTTokenUtil;
import com.likebook.util.OtpMailSender;

import lombok.AllArgsConstructor;
//Comment added
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/friend")
public class FriendController implements Serializable {
	
	private static final long serialVersionUID = -2614159153231814943L;
	
	private final FriendService friendService; 
    
	@PostMapping("/add-friend/{FriendId}")
	Response<String> addFriend(@RequestHeader (HttpHeaders.AUTHORIZATION)String token,@PathVariable("FriendId")String friendId){
		return friendService.addFriend(JWTTokenUtil.getUserIdFromToken(token.substring(7)),friendId);
	}
	@GetMapping("/my-friends")
	Response<List<Friend>> getFriendList(@RequestHeader (HttpHeaders.AUTHORIZATION)String token){
		return friendService.myFriends(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
	}
	 @GetMapping("/suggest")
	    public Response<List<User>> getAllUsersList(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
	    	return friendService.suggestFriend(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
	}
	@DeleteMapping("/{FriendId}")
	Response<String> deleteFriend(@RequestHeader (HttpHeaders.AUTHORIZATION)String token,@PathVariable("FriendId")String friendId){
		return friendService.deleteFriend(JWTTokenUtil.getUserIdFromToken(token.substring(7)),friendId);
	}
}
