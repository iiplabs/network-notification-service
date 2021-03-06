package com.iiplabs.nns.core.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "notifications")
public class Notification extends BaseModel {

	@EqualsAndHashCode.Include
	@JsonProperty("msisdnA")
	@Column(name = "destination_phone")
	private String destinationPhone;

	@EqualsAndHashCode.Include
	@JsonProperty("msisdnB")
	@Column(name = "source_phone")
	private String sourcePhone;

	@EqualsAndHashCode.Include
	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;

}
