package com.likebook.responsedto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse implements Serializable{
	
	
	private static final long serialVersionUID = 6796529076983785363L;
	
	@JsonProperty("UserId")
	private String userId;
	@JsonProperty("EmailAddress")
	private String email;
	@JsonProperty("FirstName")
	private String firstName;
	@JsonProperty("LastName")
	private String lastName;
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	@JsonProperty("DateOfBirth")
	private LocalDate dob;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("AccountType")
	private String accountType;
	@JsonProperty("Occupation")
	private String Occupation;
	@JsonProperty("Active")
	private boolean isActive;
	@JsonProperty("EmailVerified")
	private boolean isemailVerified;
	
}
