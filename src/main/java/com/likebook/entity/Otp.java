package com.likebook.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Otp implements Serializable {
	
	private static final long serialVersionUID = 4109746189640729657L;
	@Id
    @Column(unique = true)
    private String email;
    private String otp;
    private int otpCount;
    @CreationTimestamp
    private Date generatedTime;
	public Otp(String email, String otp, int otpCount,Date generatedTime) {
		super();
		this.email = email;
		this.otp = otp;
		this.otpCount = otpCount;
		this.generatedTime = generatedTime;
	}
    

}
