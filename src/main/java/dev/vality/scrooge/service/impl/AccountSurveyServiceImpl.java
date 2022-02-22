package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.account_balance.BalanceRequest;
import dev.vality.account_balance.BalanceResponse;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.scrooge.service.converter.BalanceResponseToBalanceInfoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSurveyServiceImpl implements AccountSurveyService {

    private final ClientBuilder<AccountServiceSrv.Iface> clientBuilder;
    private final BalanceResponseToBalanceInfoConverter converter;

    @Override
    public BalanceInfo getBalance(AdapterInfo adapterInfo) {
        try {
            AccountServiceSrv.Iface adapterClient = clientBuilder.build(adapterInfo.getUrl());
            BalanceRequest request = new BalanceRequest()
                    .setOptions(adapterInfo.getOptions());
            BalanceResponse balance = adapterClient.getBalance(request);
            return converter.convert(balance);
        } catch (TException e) {
            log.error("AccountSurveyServiceImpl error call adapter with url={}", adapterInfo.getUrl(), e);
            return null;
        }
    }
}
