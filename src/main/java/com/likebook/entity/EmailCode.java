package com.likebook.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class EmailCode implements Serializable {
	
	private static final long serialVersionUID = 4109746189640729657L;
	
	@Id
    @Column(unique = true)
    private String email;
    private String code;
    private int codeCount;
    @CreationTimestamp
    private Date generatedTime;
    
	public EmailCode(String email, String code, int codeCount, Date generatedTime) {
		super();
		this.email = email;
		this.code = code;
		this.codeCount = codeCount;
		this.generatedTime = generatedTime;
	}
}
