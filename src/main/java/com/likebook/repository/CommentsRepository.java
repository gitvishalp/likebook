package com.likebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.likebook.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, String>{

	@Query("SELECT c FROM Comments c WHERE c.postId = ?1 ORDER BY c.commentDate DESC")
	List<Comments> getCommentsByPostId(String postId);
	
	@Modifying
	@Query("DELETE FROM Comments c WHERE c.postId = ?1")
	int deleteByPostId(String postId);
}
