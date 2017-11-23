package net.nandgr.eth.bytecode.symexecution;

public interface Subscriber<T> {
    void inform(T element);
}
