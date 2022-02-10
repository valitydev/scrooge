package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.RouteInfo;

public interface RouteService<T> {

    RouteInfo get(T transaction);
}
