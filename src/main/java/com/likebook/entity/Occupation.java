package com.likebook.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.likebook.util.ColumnDefinition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Occupation {
	
	@Id
	@Column(columnDefinition = ColumnDefinition.NVARCHAR8)
    private String code;
	private String name;
	private String description;
	private String icon;
	@Column(name = "is_active")
	private boolean active;
	@CreationTimestamp
	private Date createdAt;
	
	 public Occupation(String code) {
	        super();
	        this.code = code;
	    }

	public Occupation(String code, String name, String description, String icon, boolean active, Date createdAt) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.active = active;
		this.createdAt = createdAt;
	}

}
