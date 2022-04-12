package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AdapterDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.service.BalanceService;
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

    private final AdapterDao adapterDao;
    private final BalanceService<Adapter> adapterBalanceService;

    @Scheduled(cron = "${service.renewal.cron}")
    @SchedulerLock(name = "balance_renewal_process")
    public void renew() {
        log.info("Start update balances");
        List<Adapter> adapters = adapterDao.getAll();
        adapters.forEach(adapterBalanceService::update);
        log.info("Finish update balances");
    }
}
