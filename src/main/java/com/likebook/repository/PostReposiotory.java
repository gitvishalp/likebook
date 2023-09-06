package com.likebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.likebook.entity.Post;

public interface PostReposiotory extends JpaRepository<Post, String> {

	List<Post> findByUserId(String userId);
	
	@Query( "SELECT p FROM Post p WHERE p.user.id in :ids" )
	List<Post> findByFriendIds(@Param("ids") List<String> friendIds);
	
	@Modifying
	@Query("DELETE FROM Post p WHERE p.user.id = ?1 ")
	int deleteByUserId(String userId);
}
