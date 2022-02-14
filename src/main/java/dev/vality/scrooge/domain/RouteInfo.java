package dev.vality.scrooge.domain;

import lombok.Data;

import java.util.Map;

@Data
public class RouteInfo {

    private String url;
    private Map<String, String> options;
    // TODO add field

}
