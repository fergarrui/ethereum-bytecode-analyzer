package net.nandgr.eth.bytecode.symexecution;

import net.nandgr.eth.bytecode.symexecution.evm.EVMEnvironment;

import java.util.ArrayList;
import java.util.List;

public class DecisionsService {

    private List<Subscriber> subscribers = new ArrayList<>();

    public synchronized void subscribe(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void addDecision(Decision decision, EVMEnvironment evmEnvironment) {
        for (Subscriber subscriber : subscribers) {
            subscriber.inform(decision, evmEnvironment);
        }
    }
}
