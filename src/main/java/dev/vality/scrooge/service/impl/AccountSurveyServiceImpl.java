package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.AccountServiceSrv;
import dev.vality.scrooge.BalanceRequest;
import dev.vality.scrooge.BalanceResponse;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.woody.api.flow.error.WRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSurveyServiceImpl implements AccountSurveyService {

    private final ClientBuilder<AccountServiceSrv.Iface> clientBuilder;
    private final Converter<BalanceResponse, BalanceInfo> converter;

    @Override
    public BalanceInfo getBalance(AdapterInfo adapterInfo) {
        String url = adapterInfo.getUrl();
        try {
            log.info("Try to get balance from adapter {}", url);
            AccountServiceSrv.Iface adapterClient = clientBuilder.build(url);
            BalanceRequest request = new BalanceRequest()
                    .setOptions(adapterInfo.getOptions());
            BalanceResponse balance = adapterClient.getBalance(request);
            BalanceInfo balanceInfo = converter.convert(balance);
            log.info("Success response from adapter {} , balanceInfo: {}", url, balanceInfo);
            return balanceInfo;
        } catch (TException | WRuntimeException e) {
            log.warn("Error call adapter with url={}, error: {}", url, e.getMessage());
            return null;
        }
    }
}
