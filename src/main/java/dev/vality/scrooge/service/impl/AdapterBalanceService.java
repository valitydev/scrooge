package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AccountDao;
import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.converter.BalanceInfoToAccountConverter;
import dev.vality.scrooge.service.converter.BalanceInfoToBalanceConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterBalanceService implements BalanceService<AdapterInfo> {

    private final BalanceDao balanceDao;
    private final AccountDao accountDao;
    private final AccountSurveyService accountSurveyService;
    private final BalanceInfoToAccountConverter accountConverter;
    private final BalanceInfoToBalanceConverter balanceConverter;

    @Override
    @Transactional
    public void update(AdapterInfo adapterInfo) {
        BalanceInfo balanceInfo = accountSurveyService.getBalance(adapterInfo);
        if (Objects.nonNull(balanceInfo)) {
            Account account = accountConverter.convert(balanceInfo, adapterInfo.getProviderId());
            Account savedAccount = accountDao.save(account);
            Balance balance = balanceConverter.convert(balanceInfo, savedAccount.getId());
            balanceDao.save(balance);
            log.info("Success update balance for account {}", account.getNumber());
        }
    }
}
