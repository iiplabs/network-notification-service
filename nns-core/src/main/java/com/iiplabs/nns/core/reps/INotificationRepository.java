package com.iiplabs.nns.core.reps;

import java.util.Collection;

import com.iiplabs.nns.core.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface INotificationRepository
		extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

	// @Transactional(readOnly=true)
	// Collection<Notification> findByInvoice(String invoice);

}
