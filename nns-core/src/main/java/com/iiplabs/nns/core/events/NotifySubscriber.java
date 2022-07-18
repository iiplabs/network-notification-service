package com.iiplabs.nns.core.events;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.iiplabs.nns.core.services.INotificationService;

@Component
public class NotifySubscriber {

    @Autowired
    private AsyncEventBus eventBus;

    @Autowired
    private INotificationService notificationService;

    @PostConstruct
    public void registerHandler() {
        eventBus.register(this);
    }

    @PreDestroy
    public void unRegisterHandler() {
        eventBus.unregister(this);
    }

    @AllowConcurrentEvents
    @Subscribe
    public void handleEvent(NotifyEvent event) {
        notificationService.notify(event.getWebId(), event.getSourcePhone(), event.getDestinationPhone());
    }

}
