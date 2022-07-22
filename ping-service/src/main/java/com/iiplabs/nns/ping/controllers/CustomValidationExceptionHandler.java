package com.iiplabs.nns.ping.controllers;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.iiplabs.nns.ping.annotations.RestControllerAnnotation;
import com.iiplabs.nns.ping.exceptions.PingServiceException;
import com.iiplabs.nns.ping.model.dto.ErrorDetails;
import com.iiplabs.nns.ping.model.dto.PingResponseDto;
import com.iiplabs.nns.ping.model.dto.PingStatus;
import com.iiplabs.nns.ping.model.dto.PingValidationResponseDto;
import com.iiplabs.nns.ping.utils.StringUtil;

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
	@ExceptionHandler(ConstraintViolationException.class)
	public final HttpEntity<PingValidationResponseDto> handleConstraintViolationException(
			ConstraintViolationException ex) {
		Map<String, String> errors = ex.getConstraintViolations().stream()
				.collect(Collectors.toMap(k -> StringUtil.getLastField(k.getPropertyPath().toString()), ConstraintViolation::getMessage));
		PingValidationResponseDto pingValidationResponseDto = new PingValidationResponseDto(errors);
		log.error("Encountered data validation problem: {}", pingValidationResponseDto);
		return new HttpEntity<>(pingValidationResponseDto);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final HttpEntity<PingValidationResponseDto> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
				.collect(Collectors.toMap(error -> StringUtil.getLastField(((FieldError) error).getField()),
						ObjectError::getDefaultMessage));
		PingValidationResponseDto pingValidationResponseDto = new PingValidationResponseDto(errors);
		log.error("Encountered method arguments validation problem: {}", pingValidationResponseDto);
		return new HttpEntity<>(pingValidationResponseDto);
	}

	/**
	 * Handles PingServiceException
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(PingServiceException.class)
	public final HttpEntity<PingResponseDto> handlesPingServiceExceptions(PingServiceException ex, WebRequest request) {
		log.error(ex.getMessage());
		PingResponseDto pingResponseDto = new PingResponseDto(PingStatus.UNAVAILABLE_SUBSCRIBER);
		return new HttpEntity<>(pingResponseDto);
	}

	/**
	 * Handles all other exceptions
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public final HttpEntity<ErrorDetails> handlesAllOtherExceptions(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails.ErrorDetailsBuilder(ex.getMessage())
				.details(request.getDescription(false))
				.timeStamp(new Date())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();
		log.error(ex, ex);
		log.error(errorDetails.toString());
		return new HttpEntity<>(errorDetails);
	}

}
