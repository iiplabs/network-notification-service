package com.iiplabs.nns.core.clients.sms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SmsClientResponse {
    
    private SendMessageStatus status;

}
