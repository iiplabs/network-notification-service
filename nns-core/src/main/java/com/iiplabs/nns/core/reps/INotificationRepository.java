package com.iiplabs.nns.core.reps;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.iiplabs.nns.core.model.Notification;

public interface INotificationRepository
		extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

	@Transactional
	Collection<Notification> deleteByWebId(String webId);

}
