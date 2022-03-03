package dev.vality.scrooge.config;

import dev.vality.damsel.payment_processing.PartyManagementSrv;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public ManagementSrv.Iface fistfulClient(@Value("${fistful.url}") Resource resource,
                                             @Value("${fistful.networkTimeout}") int networkTimeout)
            throws IOException {
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(resource.getURI())
                .build(ManagementSrv.Iface.class);
    }

    @Bean
    public PartyManagementSrv.Iface partyManagementClient(
            @Value("${party-management.url}") Resource resource,
            @Value("${party-management.networkTimeout}") int networkTimeout)
            throws IOException {
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(resource.getURI())
                .build(PartyManagementSrv.Iface.class);
    }
}
