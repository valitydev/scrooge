package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.service.Inspector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Component
public class UrlInspector implements Inspector<String> {

    @Value("#{'${adapter-client.hosts}'.split(',')}")
    private Set<String> hosts;

    @Override
    public boolean isSuitable(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            return hosts.contains(host);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url for adapter " + urlString);
        }
    }
}
