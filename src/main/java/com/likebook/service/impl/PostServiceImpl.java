package com.likebook.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.likebook.entity.Comments;
import com.likebook.entity.Friend;
import com.likebook.entity.Notification;
import com.likebook.entity.Post;
import com.likebook.entity.User;
import com.likebook.repository.CommentsRepository;
import com.likebook.repository.FriendRepository;
import com.likebook.repository.NotificationRepository;
import com.likebook.repository.PostReposiotory;
import com.likebook.repository.ProfilePictureRepository;
import com.likebook.repository.UserReposiotry;
import com.likebook.requestdto.CommentRequest;
import com.likebook.requestdto.PostRequest;
import com.likebook.responsedto.Response;
import com.likebook.service.PostService;
import com.likebook.util.ImageUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

	private static final long serialVersionUID = -3262684887199594390L;
	private final PostReposiotory postRepository;
	private final UserReposiotry userReposiotry;
	private final FriendRepository friendRepository;
	private final NotificationRepository notificationRepository;
	private final CommentsRepository commentsRepository;
	private final ProfilePictureRepository profilePictureRepository;

	@Override
	public Response<String> makePost(String userId, PostRequest request) throws IOException {
		Post post = new Post();
		Optional<User> user = userReposiotry.findById(userId);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Exists");
		}
		if(StringUtils.hasText(request.getCaption())) {
			post.setCaption(request.getCaption());
		}
		if(!request.getFile().isEmpty()) {
			post.setPicture(ImageUtil.compressImage(request.getFile().getBytes()));
		}
		post.setUser(user.get());
		post.setPostDate(new Date());
		postRepository.save(post);
		return new Response<>(HttpStatus.SC_OK,"Posted!!");
	}

	@Override
	public Response<List<Post>> getAllPost(String userId) {
		List<Post> allPosts = postRepository.findByUserId(userId);
		List<Friend> myFriends = friendRepository.findByUserId(userId);
		List<String> friendIds = new ArrayList<>();
		if(!myFriends.isEmpty()) {
			for(Friend f:myFriends) {
				String friendId = f.getFriend().getId();
				friendIds.add(friendId);
			}
			List<Post> friendPosts = postRepository.findByFriendIds(friendIds);
			allPosts.addAll(friendPosts);
		}
		
		if(allPosts.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No Post Found!!");
		}
		 for(int i=0;i<allPosts.size();i++) {
			 allPosts.get(i).setPicture(ImageUtil.decompressImage(allPosts.get(i).getPicture()));
			 allPosts.get(i).getUser().getProfile().setImageData(ImageUtil.decompressImage(allPosts.get(i).getUser().getProfile().getImageData()));
		 }
		 
		return new Response<>(HttpStatus.SC_OK,"Success",allPosts);
	}

	@Override
	public Response<String> like(String userId, String postId) {
		Optional<User> user = userReposiotry.findById(userId);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Login first");
		}
		Optional<Post> post = postRepository.findById(postId);
		if(post.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Post not exists");
		}
		post.get().setLikes(post.get().getLikes() + 1);
		postRepository.save(post.get());
		Notification notification = new Notification();
		notification.setMessage(user.get().getFirstName()+" "+user.get().getLastName()+" "+"Liked Your post"+" : "+post.get().getCaption());
		notification.setNotificationType("LIKE");
		notification.setByUser(userId);
		notification.setToUser(post.get().getUser().getId());
		notification.setDate(new Date());
		notificationRepository.save(notification);
		return new Response<>(HttpStatus.SC_OK,"Liked");
	}

	@Override
	public Response<String> comment(String userId, CommentRequest request) {
		Optional<User> user = userReposiotry.findById(userId);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Login first");
		}
		if(!StringUtils.hasText(request.getPostid()) || !StringUtils.hasText(request.getComment())) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Fields required!!");
		}
		Optional<Post> post = postRepository.findById(request.getPostid());
		if(post.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No Post Fopund");
		}
		Comments comment = new Comments();
		comment.setPostId(post.get().getId());
		comment.setComment(request.getComment());
		comment.setUser(user.get());
		comment.setCommentDate(new Date());
		commentsRepository.save(comment);
		Notification notification = new Notification();
		notification.setMessage(user.get().getFirstName()+" "+user.get().getLastName()+" "+"Commented on Your post"+" : "+post.get().getCaption()+" "+"Comment :"+ request.getComment());
		notification.setNotificationType("COMMENT");
		notification.setByUser(userId);
		notification.setToUser(post.get().getUser().getId());
		notification.setDate(new Date());
		notificationRepository.save(notification);
		return new Response<>(HttpStatus.SC_OK,"Commented");
	}

	@Override
	public Response<List<Comments>> getAllComments(String userId, String postId) {
		Optional<User> user = userReposiotry.findById(userId);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Login first!");
		}
		Optional<Post> post = postRepository.findById(postId);
		if(post.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No Post Fopund!");
		}
		List<Comments> commentList = commentsRepository.getCommentsByPostId(postId);
		if(commentList.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No Comments!");
		}
		return new Response<>(HttpStatus.SC_OK,"Success",commentList);
	}
}
