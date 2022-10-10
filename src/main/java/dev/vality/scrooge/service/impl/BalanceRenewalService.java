package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AccountDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.service.AdapterInfoService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.Inspector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceRenewalService {

    private final AccountDao accountDao;
    private final Inspector<Account> balanceUpdateInspector;
    private final AdapterInfoService adapterInfoService;
    private final BalanceService<AdapterInfo> adapterBalanceService;

    @Scheduled(cron = "${service.renewal.cron}")
    @SchedulerLock(name = "balance_renewal_process")
    public void renew() {
        log.info("Start update balances");
        List<Account> accounts = accountDao.getAll();
        for (Account account : accounts) {
            log.info("Try update balance for account with number: {}, terminal: {}",
                    account.getNumber(), account.getTerminalRef());
            if (balanceUpdateInspector.isSuitable(account)) {
                AdapterInfo adapterInfo =
                        adapterInfoService.get(account.getProviderId(), account.getTerminalRef());
                adapterBalanceService.update(adapterInfo);
            } else {
                log.info("Skip updating for account number {}", account.getNumber());
            }
        }
        log.info("Finish update balances");
    }
}
