package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalBalanceService implements BalanceService<WithdrawalTransaction> {

    private final BalanceDao balanceDao;
    private final RouteService<WithdrawalTransaction> routeService;
    private final AccountSurveyService accountSurveyService;

    @Override
    public void update(WithdrawalTransaction transaction) {
        RouteInfo routeInfo = routeService.get(transaction);
        BalanceInfo balanceInfo = accountSurveyService.getBalance(routeInfo);
        // TODO implement saving state of balance, provider?, terminal?, account?
    }
}
