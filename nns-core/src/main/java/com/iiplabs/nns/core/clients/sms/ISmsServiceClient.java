package com.iiplabs.nns.core.clients.sms;

import com.iiplabs.nns.core.clients.sms.dto.SmsClientResponseDto;

public interface ISmsServiceClient {

    SmsClientResponseDto send(String authToken, String sourcePhone, String destinationPhone, String text);

}
