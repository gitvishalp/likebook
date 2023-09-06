package com.likebook.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.likebook.entity.Friend;
import com.likebook.entity.Notification;
import com.likebook.entity.User;
import com.likebook.repository.FriendRepository;
import com.likebook.repository.NotificationRepository;
import com.likebook.repository.UserReposiotry;
import com.likebook.responsedto.Response;
import com.likebook.service.FriendService;
import com.likebook.util.ImageUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
	
	private static final long serialVersionUID = 2839404761615892673L;
    private final FriendRepository friendRepository;
	private final UserReposiotry userRepository;
	private final NotificationRepository notificationRepository;
	
	@Override
	public Response<String> addFriend(String userId, String FriendId) {
		Optional<Friend> friendOptional = friendRepository.findByUserIdAndFriendId(userId, FriendId);
		if(!friendOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"You are already Friends!");
		}
		Optional<User> userOptional = userRepository.findById(FriendId);
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Exists");
		}
		Optional<User> me = userRepository.findById(userId);
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Exists");
		}
		Friend meFriend = new Friend();
		meFriend.setUserId(userOptional.get().getId());
		meFriend.setFriend(me.get());
		meFriend.setFriendshipDate(new Date());
		friendRepository.save(meFriend);
		Friend newFriend = new Friend();
		newFriend.setUserId(userId);
		newFriend.setFriend(userOptional.get());
		newFriend.setFriendshipDate(new Date());
		friendRepository.save(newFriend);
		Notification notification = new Notification();
		notification.setMessage(me.get().getFirstName()+""+me.get().getLastName()+" "+"added you as a Friend!");
		notification.setNotificationType("FRIEND");
		notification.setByUser(userId);
		notification.setToUser(FriendId);
		notification.setDate(new Date());
		notificationRepository.save(notification);
		return new Response<>(HttpStatus.SC_OK,"Friend Added");
	}

	@Override
	public Response<List<Friend>> myFriends(String userId) {
		List<Friend> friendList = friendRepository.findByUserId(userId);
		if(friendList.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No friends");
		}
		for(Friend friend:friendList) {
			if(friend.getFriend().getProfile()!=null) {
				byte[] picture = ImageUtil.decompressImage(friend.getFriend().getProfile().getImageData());
				friend.getFriend().getProfile().setImageData(picture);
			}
		}
		return new Response<>(HttpStatus.SC_OK,"Friend List",friendList);
	}

	@Override
	public Response<String> deleteFriend(String userId,String friendId) {
		Optional<Friend> optionalFriend = friendRepository.findByUserIdAndFriendId(userId,friendId);
		Optional<Friend> meFriend  = friendRepository.findByUserIdAndFriendId(friendId, userId);
		if(optionalFriend.isEmpty() || meFriend.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No friends");
		}
		friendRepository.delete(meFriend.get());
		friendRepository.delete(optionalFriend.get());
		return new Response<>(HttpStatus.SC_OK,"Friend Removed!!");
	}
}
