package com.iiplabs.nns.core.utils;

import java.time.LocalDateTime;

import com.iiplabs.nns.core.model.dto.NotificationStatus;
import com.iiplabs.nns.core.model.dto.NotificationWebSocketMessage;

public final class NnsObjectFactory {

    private NnsObjectFactory() {
        throw new AssertionError();
    }

    public static NotificationWebSocketMessage getNotificationWebSocketMessage(String webId,
            NotificationStatus status) {
        return getNotificationWebSocketMessage(webId, null, null, status, null);
    }

    public static NotificationWebSocketMessage getNotificationWebSocketMessage(String webId,
            NotificationStatus status, String comments) {
        return getNotificationWebSocketMessage(webId, null, null, status, comments);
    }

    public static NotificationWebSocketMessage getNotificationWebSocketMessage(String webId, String sourcePhone,
            String destinationPhone, NotificationStatus status) {
        return getNotificationWebSocketMessage(webId, sourcePhone, destinationPhone, status, null);
    }

    public static NotificationWebSocketMessage getNotificationWebSocketMessage(String webId, String sourcePhone,
            String destinationPhone, NotificationStatus status, String comments) {
        NotificationWebSocketMessage message = new NotificationWebSocketMessage();
        message.setComments(comments);
        message.setDestinationPhone(destinationPhone);
        message.setSourcePhone(sourcePhone);
        message.setStatus(status);
        message.setTimeStamp(LocalDateTime.now());
        message.setWebId(webId);
        return message;
    }

}
