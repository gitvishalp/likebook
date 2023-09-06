package com.likebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likebook.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, String> {

}
