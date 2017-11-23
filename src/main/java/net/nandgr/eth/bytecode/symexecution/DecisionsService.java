package net.nandgr.eth.bytecode.symexecution;

import java.util.ArrayList;
import java.util.List;

public enum DecisionsService {

    INSTANCE;

    private List<Subscriber> subscribers = new ArrayList<>();

    public synchronized void subscribe(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void addDecision(Decision decision) {
        for (Subscriber subscriber : subscribers) {
            subscriber.inform(decision);
        }
    }
}
