package com.likebook.requestdto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest implements Serializable {

	private static final long serialVersionUID = -3644706267263585083L;
	
	@JsonProperty("EmailAddress")
	private String email;
	@JsonProperty("FirstName")
	private String firstName;
	@JsonProperty("LastName")
	private String lastName;
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	@JsonProperty("DateOfBirth")
	private Date dob;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("AccountType")
	private String accountType;
	@JsonProperty("Occupation")
	private String Occupation;
	
	public SignupRequest() {
		super();
	}

}
