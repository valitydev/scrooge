package dev.vality.scrooge.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
public class AdapterInfo {

    @ToString.Exclude
    private Map<String, String> options;
    private String url;
    private Integer termRef;
    private Integer providerId;
}
