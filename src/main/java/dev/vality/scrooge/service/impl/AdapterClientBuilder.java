package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class AdapterClientBuilder implements ClientBuilder<AccountServiceSrv.Iface> {

    @Value("${fistful.networkTimeout}")
    private int networkTimeout;

    @Override
    public AccountServiceSrv.Iface build(String url) {
        URI adapterUri = URI.create(url);
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(adapterUri)
                .build(AccountServiceSrv.Iface.class);
    }
}