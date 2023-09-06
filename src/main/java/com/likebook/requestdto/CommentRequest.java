package com.likebook.requestdto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequest implements Serializable {

	private static final long serialVersionUID = -6815142327524164910L;
	
	@JsonProperty("PostId")
	private String postid;
	@JsonProperty("Comment")
	private String comment;
}
