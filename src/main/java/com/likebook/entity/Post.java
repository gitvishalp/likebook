package com.likebook.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class Post implements Serializable {
	
	private static final long serialVersionUID = -4781560158284381256L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@ManyToOne
	private User user;
	private String caption;
	@Column(columnDefinition = "INT DEFAULT 0")
	private int likes;
	@Lob
	@Column(name = "picture",length = 3000)
	private byte[] picture;
	private Date postDate;
	
	public Post(String id, User user, String caption, byte[] picture, Date postDate) {
		super();
		this.id = id;
		this.user = user;
		this.caption = caption;
		this.picture = picture;
		
		this.postDate = postDate;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", user=" + user + ", caption=" + caption + ", likes=" + likes + ", picture="
				+ Arrays.toString(picture) + ", postDate=" + postDate + "]";
	}
	
}
