package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.account_balance.BalanceRequest;
import dev.vality.account_balance.BalanceResponse;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.converter.BalanceResponseToBalanceInfoConverter;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSurveyServiceImpl implements AccountSurveyService {

    @Value("${fistful.networkTimeout}")
    private int networkTimeout;

    private final BalanceResponseToBalanceInfoConverter converter;

    @Override
    public BalanceInfo getBalance(RouteInfo routeInfo) {
        try {
            AccountServiceSrv.Iface adapterClient = buildClient(routeInfo.getUrl());
            BalanceRequest request = new BalanceRequest();
            // TODO fill request
            BalanceResponse balance = adapterClient.getBalance(request);
            return converter.convert(balance);
        } catch (TException e) {
            log.error("AccountSurveyServiceImpl error call adapter with url={}", routeInfo.getUrl(), e);
            return null;
        }
    }

    private AccountServiceSrv.Iface buildClient(String url) {
        URI adapterUri = URI.create(url);
        return new THSpawnClientBuilder()
                .withNetworkTimeout(networkTimeout)
                .withAddress(adapterUri)
                .build(AccountServiceSrv.Iface.class);
    }
}
