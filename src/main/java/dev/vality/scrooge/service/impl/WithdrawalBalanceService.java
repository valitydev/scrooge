package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AccountDao;
import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.dao.ProviderDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
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
    private final AccountDao accountDao;
    private final ProviderDao providerDao;
    private final RouteService<WithdrawalTransaction> routeService;
    private final AccountSurveyService accountSurveyService;

    @Override
    public void update(WithdrawalTransaction transaction) {
        RouteInfo routeInfo = routeService.get(transaction);
        BalanceInfo balanceInfo = accountSurveyService.getBalance(routeInfo);
        // TODO implement saving state of balance, provider?, terminal?, account?
        updateState(transaction, balanceInfo);
    }

    private void updateState(WithdrawalTransaction transaction, BalanceInfo balanceInfo) {
        Provider provider = new Provider();
        provider.setId(transaction.getProviderId());
        provider.setName("provider"); // TODO change mandatory or fill after add new route service
        providerDao.save(provider);
        Account account = new Account();
        account.setCurrency(balanceInfo.getCurrency());
        account.setNumber(String.valueOf(balanceInfo.getAccountId()));
        account.setProviderId(provider.getId());
        Account savedAccount = accountDao.save(account);
        Balance balance = new Balance();
        balance.setAccountId(savedAccount.getId());
        balance.setTimestamp(balanceInfo.getTimestamp());
        balance.setValue(String.valueOf(balanceInfo.getAmount()));
        balanceDao.save(balance);
    }
}
