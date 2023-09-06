package com.likebook.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.likebook.util.ColumnDefinition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@DynamicUpdate
@NoArgsConstructor
public class ProfilePicture {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String type;
    @Lob
    @Column(name = "imagedata",length = 2000)
    private byte[] imageData;
	public ProfilePicture( String name, String type, byte[] imageData) {
		super();
		this.name = name;
		this.type = type;
		this.imageData = imageData;
	}
}
