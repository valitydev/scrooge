package dev.vality.scrooge.service;

public interface ClientBuilder<T> {

    T build(String url);
}
