package com.iiplabs.nns.core.reps;

import static org.assertj.core.api.Assertions.assertThat;

import com.iiplabs.nns.core.App;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@ContextConfiguration(classes = App.class)
@DataJpaTest
public class JpaTest {

	@Autowired
	private INotificationRepository notificationsRepository;

	@Test
	void injectedComponents() {
		assertThat(notificationsRepository).isNotNull();
	}

}