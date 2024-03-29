package com.iiplabs.nns.ksms.rest

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.iiplabs.nns.ksms.model.SendMessageRequestDto
import com.iiplabs.nns.ksms.model.SendMessageResponseDto

import com.iiplabs.nns.ksms.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/v1")
@RestController
class KsmsController {

    @Qualifier("smsService")
    @Autowired
    private lateinit var smsService: SmsService

    @PostMapping("/sendSms")
    suspend fun sendSms(@Valid @RequestBody sendMessageRequestDto: SendMessageRequestDto): SendMessageResponseDto {
        smsService.send(sendMessageRequestDto)
        return SendMessageResponseDto()
    }

}
