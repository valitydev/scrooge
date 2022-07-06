package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.AccountServiceSrv;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AdapterClientBuilder implements ClientBuilder<AccountServiceSrv.Iface> {

    private static final String BALANCE_PATH = "/balance";

    @Value("${adapter-client.networkTimeout}")
    private int networkTimeout;

    @Cacheable(value = "adapters", key = "#url")
    @Override
    public AccountServiceSrv.Iface build(String url) {
        URI adapterUri = URI.create(url + BALANCE_PATH);
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(adapterUri)
                .build(AccountServiceSrv.Iface.class);
    }
}
