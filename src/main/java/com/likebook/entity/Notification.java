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

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class Notification implements Serializable {

	private static final long serialVersionUID = -833905887751253420L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String NotificationType;
	private String title;
	private String message;
    private String byUser;
	private String toUser;
	@CreationTimestamp
	private Date date;
	public Notification(String id, String notificationType, String title, String message, String byUser,
			String toUser, Date date) {
		super();
		this.id = id;
		NotificationType = notificationType;
		this.title = title;
		this.message = message;
		this.byUser = byUser;
		this.toUser = toUser;
		this.date=date;
	}
	
	
}
