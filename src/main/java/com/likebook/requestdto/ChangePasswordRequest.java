package com.likebook.requestdto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequest implements Serializable {


	private static final long serialVersionUID = -6891175910215052784L;
	
	@JsonProperty("NewPassword")
	private String newPassword;

	public ChangePasswordRequest(String newPassword) {
		super();
		this.newPassword = newPassword;
	}
	
	
}
