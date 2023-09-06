package com.likebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.likebook.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {

	@Query("SELECT n FROM Notification n WHERE n.toUser = ?1 ")
	List<Notification> getNotificationByToUser(String userId);
	
	@Modifying
	@Query("DELETE FROM Notification n WHERE n.toUser = ?1")
	int deleteBytoUserId(String userId);
}
