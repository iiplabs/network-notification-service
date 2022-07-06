package com.iiplabs.nns.ping.controllers;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iiplabs.nns.ping.annotations.RestControllerAnnotation;
import com.iiplabs.nns.ping.model.dto.PingResponseDto;
import com.iiplabs.nns.ping.services.IPingService;

@Validated
@RequestMapping("/api/v1")
@RestController
@RestControllerAnnotation
public class PingController {

	@Autowired
	private IPingService pingService;

	@GetMapping("/ping")
	public ResponseEntity<PingResponseDto> ping(
			@RequestParam @Pattern(regexp = "^\\d{11}$", message = "{validation.invalid_msisdn}") String msisdn) {
		pingService.ping(msisdn);
		return ResponseEntity.ok(new PingResponseDto());
	}

}
