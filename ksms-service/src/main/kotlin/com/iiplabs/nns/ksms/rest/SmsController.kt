package com.iiplabs.nns.ksms.rest

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.iiplabs.nns.ksms.model.SendMessageRequestDto
import com.iiplabs.nns.ksms.model.SendMessageResponseDto

import com.iiplabs.nns.ksms.service.SmsService

@RestController
class KsmsController(private val smsService: SmsService) {

    @PostMapping("/sendSms")
    suspend fun sendSms(@Valid @RequestBody sendMessageRequestDto: SendMessageRequestDto): SendMessageResponseDto {
        smsService.send(sendMessageRequestDto)
        return SendMessageResponseDto()
    }

}
