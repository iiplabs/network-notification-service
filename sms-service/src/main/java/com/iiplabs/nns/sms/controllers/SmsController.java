package com.iiplabs.nns.sms.controllers;

import javax.validation.Valid;

import com.iiplabs.nns.sms.annotations.RestControllerAnnotation;
import com.iiplabs.nns.sms.model.dto.SendMessageRequestDto;
import com.iiplabs.nns.sms.model.dto.SendMessageResponseDto;
import com.iiplabs.nns.sms.services.ISmsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RestControllerAnnotation
public class SmsController {

	@Autowired
	private ISmsService smsService;

	@PostMapping("/sendSms")
	public ResponseEntity<SendMessageResponseDto> sendSms(
			@Valid @RequestBody SendMessageRequestDto sendMessageRequestDto) {
		smsService.send(sendMessageRequestDto);
		return ResponseEntity.ok(new SendMessageResponseDto());
	}

}
