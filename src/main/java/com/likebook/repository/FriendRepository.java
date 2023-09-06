package com.likebook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.likebook.entity.Friend;
import com.likebook.entity.User;

public interface FriendRepository extends JpaRepository<Friend, String> {

	@Query("SELECT f FROM Friend f WHERE f.userId = ?1 AND f.friend.id = ?2 ")
	Optional<Friend> findByUserIdAndFriendId(String userId, String FriendId);
	
	List<Friend> findByUserId(String userId);
	
	@Query("SELECT f FROM Friend f WHERE f.friend.id = ?1 ")
	List<Friend> findByFriendId(String friendId);
	
	@Modifying
	@Query("DELETE FROM Friend f WHERE f.userId = ?1")
	int deleteByUserId(String userId);
	
	@Modifying
	@Query("DELETE FROM Friend f WHERE f.friend =?1")
	int deleteByFriendId(User friend);
}
