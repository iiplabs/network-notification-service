package com.iiplabs.nns.ksms.service

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

import com.iiplabs.nns.ksms.model.SendMessageRequestDto
import org.apache.log4j.spi.LoggerFactory
import java.time.Duration.ofSeconds

interface SmsService {
    fun send(sendMessageRequestDto: SendMessageRequestDto)
}

@Service
class SmsServiceImpl : SmsService {

    companion object {
        private val MAX_EXECUTION_DELAY = 5L

        private val logger = LoggerFactory.getLogger(SmsServiceImpl::class.java)
    }

    override fun send(sendMessageRequestDto: SendMessageRequestDto) {
        logger.info(
            "Sending SMS to {} that {} is now available",
            sendMessageRequestDto.destinationPhone,
            sendMessageRequestDto.sourcePhone
        );

        val randomDelayLong = (1..MAX_EXECUTION_DELAY).shuffled().first().toLong()
        delay(ofSeconds(randomDelayLong))

        logger.info("Success sending SMS to {}, with delay {} seconds", sendMessageRequestDto.destinationPhone, randomDelayLong);
    }

}
