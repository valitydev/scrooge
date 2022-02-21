package dev.vality.scrooge.domain;

import lombok.Data;

import java.util.Map;

@Data
public class AdapterInfo {

    private Map<String, String> options;
    private String url;
}
