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
public class Friend implements Serializable{
	
	private static final long serialVersionUID = 6639005644354322349L;


	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    @ManyToOne
	private User friend;
    @CreationTimestamp
	private Date friendshipDate;
	
	public Friend(String id, String userId, User friend, Date friendshipDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.friend = friend;
		this.friendshipDate = friendshipDate;
	}
}
