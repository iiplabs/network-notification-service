package com.iiplabs.nns.core;

import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.google.common.eventbus.AsyncEventBus;
import com.iiplabs.nns.core.utils.AuditorAwareImpl;

@EnableAsync
@EnableCaching
@EnableJpaAuditing(modifyOnCreate = false, auditorAwareRef = "auditorAware")
@PropertySource("classpath:config.properties")
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

	@Bean
	public AsyncEventBus eventBus() {
		return new AsyncEventBus(Executors.newCachedThreadPool());
	}

}
