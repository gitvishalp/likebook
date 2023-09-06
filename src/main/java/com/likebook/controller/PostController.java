package com.likebook.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.likebook.entity.Comments;
import com.likebook.entity.Post;
import com.likebook.requestdto.CommentRequest;
import com.likebook.requestdto.PostRequest;
import com.likebook.responsedto.Response;
import com.likebook.service.PostService;
import com.likebook.util.JWTTokenUtil;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/post")

public class PostController implements Serializable {
	
	private static final long serialVersionUID = -176636294480794359L;
	private final PostService postService;
	
	 @PostMapping(value="",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
		     produces = {MediaType.APPLICATION_JSON_VALUE})
	    public Response<String> makePost(@RequestHeader(HttpHeaders.AUTHORIZATION)String token, PostRequest request) throws IOException{
	    	  return postService.makePost(JWTTokenUtil.getUserIdFromToken(token.substring(7)),request);
	    }
	 @GetMapping("")
	 public Response<List<Post>> getAllPost(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
		 return postService.getAllPost(JWTTokenUtil.getUserIdFromToken(token.substring(7)));
	 }
	 @PutMapping("/like/{postId}")
	 public Response<String> like(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@PathVariable("postId") String postId){
		 return postService.like(JWTTokenUtil.getUserIdFromToken(token.substring(7)), postId);
	 }
	 @PutMapping("/comment")
	 public Response<String> comment(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@Valid @RequestBody CommentRequest request){
		 return postService.comment(JWTTokenUtil.getUserIdFromToken(token.substring(7)), request);
	 }
	 @GetMapping("/comments/{postId}")
	 public Response<List<Comments>> getAllComments(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,@PathVariable("postId") String postId){
		 return postService.getAllComments(JWTTokenUtil.getUserIdFromToken(token.substring(7)), postId);
	 }
}
