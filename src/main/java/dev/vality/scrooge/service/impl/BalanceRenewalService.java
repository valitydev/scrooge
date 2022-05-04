package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AdapterDao;
import dev.vality.scrooge.dao.BalanceDao;
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
    private final BalanceDao balanceDao;
    private final DurationInspector durationInspector;
    private final BalanceService<Adapter> adapterBalanceService;

    @Scheduled(cron = "${service.renewal.cron}")
    @SchedulerLock(name = "balance_renewal_process")
    public void renew() {
        log.info("Start update balances");
        List<Adapter> adapters = adapterDao.getAll();
        for (Adapter adapter : adapters) {
            LocalDateTime lastBalanceUpdateTime = balanceDao.getUpdateTimeByProvider(adapter.getProviderId());
            log.info("Last balance update time for adapter {}", lastBalanceUpdateTime);
            long renewalDuration = MINUTES.between(lastBalanceUpdateTime, LocalDateTime.now().truncatedTo(SECONDS));
            log.info("Duration is {}", renewalDuration);
            if (durationInspector.isValid(renewalDuration)) {
                adapterBalanceService.update(adapter);
            } else {
                log.info("Skip adapter {}", adapter.getUrl());
            }
        }
        log.info("Finish update balances");
    }
}
