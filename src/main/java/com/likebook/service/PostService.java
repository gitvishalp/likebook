package com.likebook.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.likebook.entity.Comments;
import com.likebook.entity.Post;
import com.likebook.requestdto.CommentRequest;
import com.likebook.requestdto.PostRequest;
import com.likebook.responsedto.Response;

public interface PostService extends Serializable {
	
	Response<String> makePost(String userId,PostRequest request) throws IOException;
	
	Response<List<Post>> getAllPost(String userId);
	
	Response<String> like(String userId,String postId);
	Response<String> comment(String userId,CommentRequest request);
	Response<List<Comments>> getAllComments(String userId,String postId);
}
