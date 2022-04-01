package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.config.properties.AdapterClientProperties;
import dev.vality.scrooge.service.Inspector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UrlInspector implements Inspector<String> {

    private final AdapterClientProperties properties;

    @Override
    public boolean isSuitable(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            Set<String> availableHosts = properties.getAvailableHosts();
            return availableHosts.contains(host);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url for adapter " + urlString);
        }
    }
}
