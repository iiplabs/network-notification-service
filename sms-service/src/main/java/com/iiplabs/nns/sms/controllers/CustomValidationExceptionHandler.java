package com.iiplabs.nns.sms.controllers;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.iiplabs.nns.sms.annotations.RestControllerAnnotation;
import com.iiplabs.nns.sms.model.dto.ErrorDetails;
import com.iiplabs.nns.sms.model.dto.SendMessageValidationResponseDto;
import com.iiplabs.nns.sms.utils.StringUtil;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice(annotations = { RestControllerAnnotation.class })
public class CustomValidationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final HttpEntity<SendMessageValidationResponseDto> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
				.collect(Collectors.toMap(error -> StringUtil.getLastField(((FieldError) error).getField()),
						ObjectError::getDefaultMessage));
		SendMessageValidationResponseDto sendMessageValidationResponseDto = new SendMessageValidationResponseDto(errors);
		log.error("Encountered data validation problem: {}", sendMessageValidationResponseDto);
		return new HttpEntity<>(sendMessageValidationResponseDto);
	}

	/**
	 * Handles all exceptions, including custom SmsServiceException
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public final HttpEntity<ErrorDetails> allExceptions(Exception e, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails.ErrorDetailsBuilder(e.getMessage())
				.details(request.getDescription(false))
				.timeStamp(new Date())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();
		log.error(e, e);
		log.error(errorDetails.toString());
		return new HttpEntity<>(errorDetails);
	}

}
