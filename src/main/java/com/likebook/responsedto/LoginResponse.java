package com.likebook.responsedto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse implements Serializable {

	private static final long serialVersionUID = 7277043375150108971L;
	@JsonProperty("Token")
    private String token;
    @JsonProperty("IsEmailVerified")
    private boolean emailVerified;
}
