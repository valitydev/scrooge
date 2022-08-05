package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AccountDao;
import dev.vality.scrooge.dao.AdapterDao;
import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.DurationInspector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceRenewalService {

    private final AdapterDao adapterDao;
    private final AccountDao accountDao;
    private final BalanceDao balanceDao;
    private final DurationInspector durationInspector;
    private final BalanceService<Adapter> adapterBalanceService;

    @Scheduled(cron = "${service.renewal.cron}")
    @SchedulerLock(name = "balance_renewal_process")
    public void renew() {
        log.info("Start update balances");
        List<Account> accounts = accountDao.getAll();
        for (Account account : accounts) {
            log.info("Try update balance for account with number: {}", account.getNumber());
            LocalDateTime lastBalanceUpdateTime = balanceDao.getUpdateTimeByAccount(account.getId());
            log.info("Last balance update time {} for account number {}", lastBalanceUpdateTime, account.getNumber());
            long renewalDuration = MINUTES.between(lastBalanceUpdateTime, LocalDateTime.now().truncatedTo(SECONDS));
            log.info("Duration until last balance update is {} minutes for account number {}",
                    renewalDuration, account.getNumber());
            if (durationInspector.isValid(renewalDuration)) {
                Adapter adapter = adapterDao.getByProviderId(account.getProviderId());
                adapterBalanceService.update(adapter);
            } else {
                log.info("Skip updating for account number {}", account.getNumber());
            }
        }
        log.info("Finish update balances");
    }
}
