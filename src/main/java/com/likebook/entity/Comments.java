package com.likebook.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
@DynamicUpdate
@NoArgsConstructor
public class Comments implements Serializable {
	private static final long serialVersionUID = 3066363473267434359L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String postId;
	private String comment;
	@ManyToOne
	private User user;
	@CreationTimestamp
	private Date commentDate;
	public Comments(String id, String postId, String comment, Date commentDate) {
		super();
		this.id = id;
		this.postId = postId;
		this.comment = comment;
		this.commentDate = commentDate;
	}
	
}
