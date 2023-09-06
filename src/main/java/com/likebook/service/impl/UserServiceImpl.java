package com.likebook.service.impl;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.likebook.entity.AccountType;
import com.likebook.entity.EmailCode;
import com.likebook.entity.Friend;
import com.likebook.entity.Notification;
import com.likebook.entity.Occupation;
import com.likebook.entity.Otp;
import com.likebook.entity.Post;
import com.likebook.entity.ProfilePicture;
import com.likebook.entity.User;
import com.likebook.repository.AccountTypeRepository;
import com.likebook.repository.CommentsRepository;
import com.likebook.repository.EmailCodeRepository;
import com.likebook.repository.FriendRepository;
import com.likebook.repository.NotificationRepository;
import com.likebook.repository.OccupationRepository;
import com.likebook.repository.OtpRepository;
import com.likebook.repository.PostReposiotory;
import com.likebook.repository.ProfilePictureRepository;
import com.likebook.repository.UserReposiotry;
import com.likebook.requestdto.OtpRequest;
import com.likebook.requestdto.SignupRequest;
import com.likebook.requestdto.UpdateDetailsRequest;
import com.likebook.requestdto.ChangePasswordRequest;
import com.likebook.requestdto.CommentRequest;
import com.likebook.requestdto.EmailVerification;
import com.likebook.requestdto.LoginRequest;
import com.likebook.requestdto.LoginWithCodeRequest;
import com.likebook.responsedto.LoginResponse;
import com.likebook.responsedto.Response;
import com.likebook.responsedto.UserResponse;
import com.likebook.service.UserService;
import com.likebook.util.GenerateOtp;
import com.likebook.util.ImageUtil;
import com.likebook.util.JWTTokenUtil;
import com.likebook.util.OtpMailSender;

import feign.form.ContentType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	private static final long serialVersionUID = -4458354549819532436L;
	
	private final OtpRepository otpRepository;
	private final EmailCodeRepository emailCodeRepository;
	private final UserReposiotry userRepository;
    private final OtpMailSender otpMailSender;
    private final GenerateOtp generateOtp;
    private final AccountTypeRepository accountTypeRepository;
    private final OccupationRepository occupationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtil jwtToken;
    private final ProfilePictureRepository profileRepository;
    private final NotificationRepository notificationRepository;
    private final PostReposiotory postRespository;
    private final FriendRepository friendRepository;
    private final CommentsRepository commentsRepository;
    
	@Override
	public Response<String> sendOTPEmail(OtpRequest request) throws UnsupportedEncodingException, MessagingException{
		
		if(!StringUtils.hasText(request.getEmail())) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Enter Valid Email!");
		}
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(request.getEmail());  
		if(!matcher.matches()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Enter Valid Email!");
		}
		Optional<User> user= userRepository.findByEmail(request.getEmail());
		if(!user.isEmpty()) {
			if(user.get().isActive()) {
				return new Response<>(HttpStatus.SC_ACCEPTED,"User Already Created!! Try Login");
			}
		}
		Otp otp = new Otp();
		Optional<Otp> otpOptional = otpRepository.findById(request.getEmail());
		String OneTimePassword = generateOtp.generateOtp().get().toString();
		if(!otpOptional.isEmpty()) {
			 final long OTP_VALID_DURATION = 5 * 60 * 1000;
			 long currentTimeInMillis = System.currentTimeMillis();
			 long otpRequestedTimeInMillis = otpOptional.get().getGeneratedTime().getTime();
			if(otpOptional.get().getOtpCount()>5 && otpRequestedTimeInMillis + OTP_VALID_DURATION > currentTimeInMillis  ) {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"OTP limit exceed!! Try after 5 mins");
			}
			otp.setOtpCount(otpOptional.get().getOtpCount() + 1 );
		}else {
			otp.setOtpCount(otp.getOtpCount() + 1);
		}
		otp.setOtp(OneTimePassword);
		otp.setEmail(request.getEmail());
		otp.setGeneratedTime(new Date());
		//String isMailSend = otpMailSender.sendMail(request.getEmail(), OneTimePassword);
		otpRepository.save(otp);
		return new Response<>("sent db");
	}

	@Override
	public Response<String> emailVerification(EmailVerification request) {
		if(request.getEmail()==null || request.getOtp()==null) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Fields Required");
		}
		Optional<Otp> otp = otpRepository.findById(request.getEmail());
		if(otp.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid OTP");
		}
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if(!userOptional.isEmpty()) {
			if(userOptional.get().isActive()) {
				 otp.get().setOtpCount(0);
		    	 otp.get().setOtp(null);
		    	 otpRepository.save(otp.get());
				return new Response<>(HttpStatus.SC_ACCEPTED,"User Already Created!! Try Login");
			}
			if(userOptional.get().isEmailVerified()) {
				 otp.get().setOtpCount(0);
		    	 otp.get().setOtp(null);
		    	 otpRepository.save(otp.get());
				return new Response<>(HttpStatus.SC_OK,"Email Already Verified!!");
			}
		}
		if(otp.get().getOtp()==null) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid OTP");
		}
			if(otp.get().getOtp().equals(request.getOtp())) {
				 final long OTP_VALID_DURATION = 5 * 60 * 1000;
				 long currentTimeInMillis = System.currentTimeMillis();
				 long otpRequestedTimeInMillis = otp.get().getGeneratedTime().getTime();
				 if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
					 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"OTP Expired!!");
			     }
				     User user = new User();
			    	 user.setEmail(request.getEmail());
			    	 user.setActive(false);
			    	 user.setCreatedAt(new Date());
			    	 user.setEmailVerified(true);
			    	 otp.get().setOtpCount(0);
			    	 otp.get().setOtp(null);
			    	 userRepository.save(user);
			    	 otpRepository.save(otp.get());
			    	 return new Response<>(HttpStatus.SC_OK,"Email Verified!!");
				
			}else {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid OTP!!");
			}
		
	}

	@Override
	public Response<UserResponse> createUser(SignupRequest request) {
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Email Not Verified!!");
		}
		if(userOptional.get().isActive()) {
			return new Response<>(HttpStatus.SC_ACCEPTED,"User Already Created for this Email!! Try Login");
		}
			
				Optional<AccountType> accountType = accountTypeRepository.findById(request.getAccountType());
				if(accountType.isEmpty()) {
					return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid AccountType!!");
				}
				Optional<Occupation> occupation = occupationRepository.findById(request.getOccupation());
				if(occupation.isEmpty()) {
					return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid Occupation!!");
				}
				userOptional.get().setAccountType(accountType.get());
				userOptional.get().setOccupation(occupation.get());
				userOptional.get().setFirstName(request.getFirstName());
				userOptional.get().setLastName(request.getLastName());
				userOptional.get().setPhoneNumber(request.getPhoneNumber());
				userOptional.get().setActive(true);
				userOptional.get().setCountry(request.getCountry());
				userOptional.get().setCreatedAt(new Date());
				LocalDate dob = request.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				userOptional.get().setDateOfBirth(dob);
				userOptional.get().setPassword(passwordEncoder.encode(request.getPassword().trim()));
				userRepository.save(userOptional.get());
		        UserResponse response = userRepository.getUserDetails(userOptional.get().getId());
				return new Response<>(HttpStatus.SC_OK,"Success",response);
          } 

	@Override
	public Response<LoginResponse> login(LoginRequest request, String IpAddress) {
		if(!StringUtils.hasText(request.getEmail())
				|| !StringUtils.hasText(request.getPassword())) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Fields Required!!");
		}
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!! Try sign up");
		}
		if(!userOptional.get().isActive()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!! Try sign up");
		}
			 if(passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())){
				 LoginResponse response = new LoginResponse(jwtToken.generateToken(request.getEmail(), userOptional.get().getId(), userOptional.get().getPhoneNumber(), userOptional.get().getPassword(), IpAddress),userOptional.get().isEmailVerified());
				 return new Response<>(HttpStatus.SC_OK,"Sucess",response);
			 }else {
				 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid Password!!");
			 }
		}
	

	@Override
	public Response<LoginResponse> loginWithCode(LoginWithCodeRequest request,String IpAddress){
		if(!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getCode())
				){
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Fields Required!!");
		}
		Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!! Try sign up");
		}
		if(!userOptional.get().isActive()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!! Try sign up");
		}
		Optional<EmailCode> code = emailCodeRepository.findById(request.getEmail());
		if(code.isEmpty() || code.get().getCode()== null) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid Code!");
		}
		if(request.getCode().equals(code.get().getCode())) {
			 LoginResponse response = new LoginResponse(jwtToken.generateToken(request.getEmail(), userOptional.get().getId(), userOptional.get().getPhoneNumber(), userOptional.get().getPassword(), IpAddress),userOptional.get().isEmailVerified());
			 code.get().setCode(null);
			 code.get().setCodeCount(0);
			 emailCodeRepository.save(code.get());
			 return new Response<>(HttpStatus.SC_OK,"Sucess",response);
		}else {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid Code");
		}
	}
	
	@Override
	public Response<UserResponse> userDetails(String userId) {
		Optional<UserResponse> response = userRepository.getUserDetailsById(userId);
		if(response.isEmpty()) {
			return new Response<>(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,"Unauthorized!!");
		}
		if(!response.get().isActive()) {
			return new Response<>(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,"User Not Found!!");
		}
		return new Response<>(HttpStatus.SC_OK,"Success",response.get());
	}

	@Override
	public Response<UserResponse> userDetailsByEmail(String email, String userId) {
		Optional<UserResponse> response = userRepository.getUserDetailsByEmailAndId(userId,email);
		if(response.isEmpty()) {
			return new Response<>(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,"Unauthorized!!");
		}
		if(!response.get().isActive()) {
			return new Response<>(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,"User Not Found!!");
		}
		return new Response<>(HttpStatus.SC_OK,"Success",response.get());
	}

	@Override
	public Response<String> forgotPassword(String email)
			throws UnsupportedEncodingException, MessagingException {
		Optional<User> user = userRepository.findByEmail(email);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!!");
		}
		if(!user.get().isActive()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!");
		}
		EmailCode code = new EmailCode();
		Optional<EmailCode> codeOptional = emailCodeRepository.findById(email);
		String OneTimeCode = generateOtp.generateCode();
		if(!codeOptional.isEmpty()) {
			 final long OTP_VALID_DURATION = 5 * 60 * 1000;
			 long currentTimeInMillis = System.currentTimeMillis();
			 long otpRequestedTimeInMillis = codeOptional.get().getGeneratedTime().getTime();
			if(codeOptional.get().getCodeCount()>5 && otpRequestedTimeInMillis + OTP_VALID_DURATION > currentTimeInMillis  ) {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Code limit exceed!! Try after 5 mins");
			}
			code.setCodeCount(codeOptional.get().getCodeCount() + 1 );
		}else {
			code.setCodeCount(code.getCodeCount() + 1);
		}
		code.setCode(OneTimeCode);
		code.setEmail(email);
		code.setGeneratedTime(new Date());
		//String isMailSend = otpMailSender.sendCode(email, OneTimeCode);
		emailCodeRepository.save(code);
		userRepository.save(user.get());
		return new Response<>("Mail send");
	}

	@Override
	public Response<String> changePassword(String userId, ChangePasswordRequest request) {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_FORBIDDEN,"UnAuthorized");
		}
		if(!userOptional.get().isActive()) {
			return new Response<>(HttpStatus.SC_FORBIDDEN,"User Not Found!");
		}
		if(!StringUtils.hasText(request.getNewPassword())) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Make Strong Password!");
		}
		userOptional.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(userOptional.get());
		return new Response<>(HttpStatus.SC_OK,"Password Changed");
	}

	@Override
	@Transactional
	public Response<String> deleteUser(String userId, String email) {
		Optional<User> userOptional = userRepository.findByIdAndEmail(userId, email);
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_FORBIDDEN,"UnAuthorized");
		}
		if(!userOptional.get().isActive()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not Found!");
		}
		String profileId=null;
		if(userOptional.get().getProfile()!=null) {
			profileId = userOptional.get().getProfile().getId();
			userOptional.get().setProfile(null);
			userRepository.save(userOptional.get());
			profileRepository.deleteById(profileId);
		}
	    List<Post> postList = postRespository.findByUserId(userId);
	    if(!postList.isEmpty()) {
	    	for(Post p:postList) {
	    		commentsRepository.deleteByPostId(p.getId());
	    	}
	    	postRespository.deleteByUserId(userId);
	    }
	    List<Friend> friendList = friendRepository.findByUserId(userId);
	    if(!friendList.isEmpty()) {
	    	friendRepository.deleteByUserId(userId);
	    	friendRepository.deleteByFriendId(userOptional.get());
	    }
	    List<Notification> notificationList = notificationRepository.getNotificationByToUser(userId);
	    if(!notificationList.isEmpty()) {
	    	notificationRepository.deleteBytoUserId(userId);
	    }
		userRepository.delete(userOptional.get());
		return new Response<>(HttpStatus.SC_OK,"User deleted");
	}

	@Override
	public Response<String> uploadProfilePic(String userId,MultipartFile file) throws IOException {
			Optional<User> user = userRepository.findById(userId);
			if(user.isEmpty()) {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User not found!!");
			}
			
			
			if(user.get().getProfile()!=null) {
				String profileId =user.get().getProfile().getId();
				user.get().setProfile(null);
				userRepository.save(user.get());
				profileRepository.deleteById(profileId);
			}
			ProfilePicture pic= new ProfilePicture();
			pic.setName(file.getOriginalFilename());
			pic.setType(file.getContentType());
			pic.setImageData(ImageUtil.compressImage(file.getBytes()));
			user.get().setProfile(pic);
			userRepository.save(user.get());
			return new Response<>(HttpStatus.SC_OK,"Profile Picture Uploaded");
	}

	@Override
	public Response<byte[]> downloadProfilePic(String userId) {
	
			Optional<User> user = userRepository.findById(userId);
			
			if(user.isEmpty()) {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not found!!");
			}
			if(user.get().getProfile()==null) {
				return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"no profile");
			}
			byte[] picture = ImageUtil.decompressImage(user.get().getProfile().getImageData());
			return new Response<>(HttpStatus.SC_OK,"Success",picture);
		
	}

	@Override
	public Response<String> deleteProfilePic(String userId) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"User Not found!!");
		}
		if(user.get().getProfile()==null) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"no profile");
		}
		String profileId =user.get().getProfile().getId();
		user.get().setProfile(null);
		userRepository.save(user.get());
		profileRepository.deleteById(profileId);
		return new Response<>(HttpStatus.SC_OK,"Profile removed!!");
	}

	@Override
	public Response<String> updateDetails(String userId, UpdateDetailsRequest request) {
		Optional<User> userOptional = userRepository.findById(userId);
		if(userOptional.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No User Found!!");
		}			
				Optional<AccountType> accountType = accountTypeRepository.findById(request.getAccountType());
				if(accountType.isEmpty()) {
					return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid AccountType!!");
				}
				Optional<Occupation> occupation = occupationRepository.findById(request.getOccupation());
				if(occupation.isEmpty()) {
					return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Invalid Occupation!!");
				}
				userOptional.get().setAccountType(accountType.get());
				userOptional.get().setOccupation(occupation.get());
				userOptional.get().setFirstName(request.getFirstName());
				userOptional.get().setLastName(request.getLastName());
				userOptional.get().setPhoneNumber(request.getPhoneNumber());
				userOptional.get().setActive(true);
				userOptional.get().setCountry(request.getCountry());
				LocalDate dob = request.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				userOptional.get().setDateOfBirth(dob);
				userOptional.get().setPassword(passwordEncoder.encode(request.getPassword().trim()));
				userRepository.save(userOptional.get());
		return new Response<>(HttpStatus.SC_OK,"Success");
	}

	@Override
	public Response<List<User>> getAllUsers(String userId) {
		List<User> users = userRepository.getAllUsers();
		if(users.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No user found");
		}
		for(User u:users) {
			if(u.getProfile()!=null) {
				byte[] picture = ImageUtil.decompressImage(u.getProfile().getImageData());
				u.getProfile().setImageData(picture);
			}
		}
		
		return new Response<>(HttpStatus.SC_OK,"Success",users);
	}

	@Override
	public Response<List<User>> serachUserByName(String name) {
		 String names[]= name.split(" "); 
	     List<User> users = userRepository.searchUserByName(names[0]);
	     if(users.isEmpty()){
	    	 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No user found"); 
	     }
	     for(User u:users) {
				if(u.getProfile()!=null) {
					byte[] picture = ImageUtil.decompressImage(u.getProfile().getImageData());
					u.getProfile().setImageData(picture);
				}
		}
	     return new Response<>(HttpStatus.SC_OK,"Success",users);
	}

	@Override
	public Response<List<Notification>> getAllNotification(String userId) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
			 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No user found"); 
		}
		List<Notification> notificationList = notificationRepository.getNotificationByToUser(userId);
		if(notificationList.isEmpty()) {
			return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No Notifications"); 
		}
		notificationList.forEach(n->{
			if(n.getNotificationType().equals("FRIEND")){
				String emoji= new String(Character.toChars(0x1F465));
				n.setTitle(emoji+" "+ "New Friend!!");
			}else if (n.getNotificationType().equals("LIKE")) {
				String emoji= new String(Character.toChars(0x1F44D));
				n.setTitle(emoji+" "+ "New Like!!");
			}else if(n.getNotificationType().equals("COMMENT")) {
				String emoji= new String(Character.toChars(0x1F4AC));
				n.setTitle(emoji+" "+ "New Comment!!");
			}
		});
		return new Response<>(HttpStatus.SC_OK,"Success",notificationList);
	}

	@Override
	public Response<String> deleteNotification(String userId, String notificationId) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
			 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"No user found"); 
		}
		Optional<Notification> notification= notificationRepository.findById(notificationId);
		if(notification.isEmpty()) {
			 return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Notification not found"); 
		}
		notificationRepository.delete(notification.get());
		return new Response<>(HttpStatus.SC_OK,"Success"); 
	}
	}