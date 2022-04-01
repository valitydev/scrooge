package dev.vality.scrooge.service;

public interface Inspector<T> {

    boolean isSuitable(T object);
}
