package com.likebook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likebook.entity.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, String> {
	
	Optional<ProfilePicture> findByName(String fileName);
}
