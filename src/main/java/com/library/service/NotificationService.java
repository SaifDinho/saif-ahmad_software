package com.library.service;

import com.library.notification.NotificationObserver;
import com.library.notification.NotificationEvent;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void attach(NotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(NotificationEvent event) {
        for (NotificationObserver observer : observers) {
            observer.update(event);
        }
    }

    public void sendNotification(NotificationEvent event) {
        notifyObservers(event);
    }

    public int getObserverCount() {
        return observers.size();
    }
}
