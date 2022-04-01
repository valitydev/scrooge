package dev.vality.scrooge.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "adapter-client")
public class AdapterClientProperties {

    private Set<String> availableHost;
    private int networkTimeout;
}
