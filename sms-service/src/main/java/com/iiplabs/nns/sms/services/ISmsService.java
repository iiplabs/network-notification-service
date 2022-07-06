package com.iiplabs.nns.sms.services;

import com.iiplabs.nns.sms.model.dto.SendMessageRequestDto;

public interface ISmsService {

  void send(SendMessageRequestDto sendMessageRequestDto);

}
