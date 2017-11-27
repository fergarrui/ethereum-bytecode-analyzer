package net.nandgr.eth.bytecode.symexecution;

public interface Subscriber<T,E> {
    void inform(T element, E inputs);
}
