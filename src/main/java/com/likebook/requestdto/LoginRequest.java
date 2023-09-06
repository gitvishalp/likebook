package com.likebook.requestdto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest implements Serializable {

	
	private static final long serialVersionUID = -2662020202556723050L;
	
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Password")
	private String password;
}
