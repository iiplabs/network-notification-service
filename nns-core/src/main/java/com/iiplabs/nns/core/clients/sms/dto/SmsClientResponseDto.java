package com.iiplabs.nns.core.clients.sms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SmsClientResponseDto {
    
    private SendMessageStatus status;

}
