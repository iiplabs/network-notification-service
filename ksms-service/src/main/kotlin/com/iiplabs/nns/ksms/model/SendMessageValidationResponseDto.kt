package com.iiplabs.nns.ksms.model

import kotlin.collections.MutableMap

data class SendMessageValidationResponseDto(
    val errors: Map<String, String?>
)