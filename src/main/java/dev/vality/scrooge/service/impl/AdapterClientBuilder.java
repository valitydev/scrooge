package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.scrooge.config.properties.AdapterClientProperties;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AdapterClientBuilder implements ClientBuilder<AccountServiceSrv.Iface> {

    private static final String BALANCE_PATH = "/balance";

    private final AdapterClientProperties properties;

    @Cacheable(value = "adapters", key = "#url")
    @Override
    public AccountServiceSrv.Iface build(String url) {
        URI adapterUri = URI.create(url + BALANCE_PATH);
        return new THSpawnClientBuilder()
                .withNetworkTimeout(properties.getNetworkTimeout())
                .withAddress(adapterUri)
                .build(AccountServiceSrv.Iface.class);
    }
}
