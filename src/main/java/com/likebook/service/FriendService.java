package com.likebook.service;

import java.io.Serializable;
import java.util.List;
import com.likebook.entity.Friend;
import com.likebook.responsedto.Response;

public interface FriendService extends Serializable{
	
	Response<String> addFriend(String userId, String FriendId);
	Response<List<Friend>> myFriends(String userId);
	Response<String> deleteFriend(String userId,String friendId);

}
