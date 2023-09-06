package com.likebook.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class OtpMailSender implements Serializable {

	private static final long serialVersionUID = -7669658508063258406L;
	private final JavaMailSender mailSender;
	
	public String sendMail(String senderEmail, String Otp) throws UnsupportedEncodingException, MessagingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();           
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    helper.setFrom("noreply@likebook");
		    helper.setTo(senderEmail);
		     
		    String subject = "LikeBook- One Time Password (OTP)";
		     
		    String content = "<p>Hello " + senderEmail + "</p>"
		            + "<p>We are happy that you are going to use LikeBook."
		            + "<br>Your One Time Password is:</p>"
		            + "<p><b> " + "<font face=\"Verdana\" size =\"5\" color=\"green\" >"  + Otp + "</font></b></p>"
		            + "<p><b> Don't shere it with anyone!! </b></p>" 
		            + "<p><font color=\"red\"> Note: this OTP is set to expire in 5 minutes. </font></p>"
		            + "<br>"
		            + "<b>Team LikeBook<b>";
		    helper.setSubject(subject);
		    helper.setText(content, true);
		    mailSender.send(message); 
			return "OTP Sent";
		}catch(Exception ex) {
			return ex.toString();
		}
		
	}
	
	
	public String sendCode(String senderEmail, String code) throws UnsupportedEncodingException, MessagingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();           
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    helper.setFrom("noreply@likebook");
		    helper.setTo(senderEmail);
		     
		    String subject = "LikeBook- ForgetPassword Code";
		     
		    String content = "<p>Hello " + senderEmail + "</p>"
		            + "<p> Sorry to see that you are facing problem to get into your account."
		            + "<br>Your Temporary Password is:</p>"
		            + "<p><b> " + "<font face=\"Verdana\" size =\"5\" >" + code + "</font></b></p>"
		            + "<p><b> Use this code to login </b></p>" 
		            + "<p><font color=\"red\"> this code is valid only for first login. </font></p>"
		            + "<br>"
		            + "<b>Team LikeBook<b>";
		    helper.setSubject(subject);
		    helper.setText(content, true);
		    mailSender.send(message); 
			return "Code send to registered Email!! ";
		}catch(Exception ex) {
			return ex.toString();
		}
		
	}
	
	
	
	
}
