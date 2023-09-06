package com.likebook.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.likebook.util.ColumnDefinition;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class User implements Serializable {

	private static final long serialVersionUID = -1579188418634296264L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
    private String password;
    @OneToOne(cascade=CascadeType.MERGE)
    private ProfilePicture profile;
	@Column(columnDefinition = ColumnDefinition.BIT)
	private boolean isEmailVerified;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int failedLoginAttempt;
    private LocalDate dateOfBirth;
    private String country;
    @Column(name = "is_active")
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "account_type", columnDefinition = ColumnDefinition.NVARCHAR8)
    private AccountType accountType;
    @ManyToOne
    @JoinColumn(name = "occupation", columnDefinition = ColumnDefinition.NVARCHAR8)
    private Occupation occupation;
    @CreationTimestamp
    private Date createdAt;
}
