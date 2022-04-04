package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WithdrawalBalanceService implements BalanceService<WithdrawalTransaction> {

    private final RouteService<WithdrawalTransaction> routeService;
    private final AccountSurveyService accountSurveyService;
    private final StateService stateService;
    private final Inspector<String> urlInspector;

    @Override
    public void update(WithdrawalTransaction transaction) {
        RouteInfo routeInfo = routeService.get(transaction);
        AdapterInfo adapterInfo = routeInfo.getAdapterInfo();
        if (!urlInspector.isSuitable(adapterInfo.getUrl())) {
            return;
        }
        BalanceInfo balanceInfo = accountSurveyService.getBalance(adapterInfo);
        if (Objects.nonNull(balanceInfo)) {
            stateService.update(routeInfo, balanceInfo);
        }
    }
}
