package com.likebook.requestdto;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequest implements Serializable {
	
	private static final long serialVersionUID = 3479688534553709972L;
   
	private String caption;
	private MultipartFile file;

	public PostRequest(String caption) {
		super();
		this.caption = caption;
	}
}
