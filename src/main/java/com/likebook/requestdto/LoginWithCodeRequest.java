package com.likebook.requestdto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginWithCodeRequest {

	
private static final long serialVersionUID = -2662020202556723050L;
	
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Code")
	private String code;
}
