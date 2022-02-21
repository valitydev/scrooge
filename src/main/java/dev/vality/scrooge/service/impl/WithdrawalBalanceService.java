package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.RouteService;
import dev.vality.scrooge.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WithdrawalBalanceService implements BalanceService<WithdrawalTransaction> {

    private final RouteService<WithdrawalTransaction> routeService;
    private final AccountSurveyService accountSurveyService;
    private final StateService stateService;

    @Override
    public void update(WithdrawalTransaction transaction) {
        RouteInfo routeInfo = routeService.get(transaction);
        BalanceInfo balanceInfo = accountSurveyService.getBalance(routeInfo.getAdapterInfo());
        if (Objects.nonNull(balanceInfo)) {
            stateService.update(routeInfo, balanceInfo);
        }
    }
}
