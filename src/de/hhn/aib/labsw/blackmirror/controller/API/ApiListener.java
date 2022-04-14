package de.hhn.aib.labsw.blackmirror.controller.API;

public interface ApiListener<T> {
    Class<T> theClass;
    public void dataReceived(String topic, T t);
}
