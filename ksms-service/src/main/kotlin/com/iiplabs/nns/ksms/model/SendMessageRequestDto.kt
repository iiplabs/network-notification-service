package com.iiplabs.nns.ksms.model

data class SendMessageRequestDto(
        val sourcePhone: String,
        val destinationPhone: String,
        val text: String
)