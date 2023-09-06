package com.likebook.requestdto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpRequest implements Serializable {
 
	
	private static final long serialVersionUID = 8709223710773425466L;
	@JsonProperty("Email")
	private String email;
	
	public OtpRequest(String email) {
		super();
		this.email=email;
	}
	
}
