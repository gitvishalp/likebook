package com.likebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likebook.entity.EmailCode;

public interface EmailCodeRepository extends JpaRepository<EmailCode, String>{

	
}
