package com.likebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likebook.entity.Occupation;

public interface OccupationRepository extends JpaRepository<Occupation, String>{

}
