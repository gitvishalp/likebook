package com.likebook.requestdto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerification implements Serializable {

	private static final long serialVersionUID = -8333908583095886732L;
	
	@JsonProperty("EmailAddress")
	private String email;
	@JsonProperty("OTP")
	private String Otp;
	

}
