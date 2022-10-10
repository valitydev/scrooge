package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.service.DurationInspector;
import dev.vality.scrooge.service.Inspector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceUpdateInspector implements Inspector<Account> {

    private final BalanceDao balanceDao;
    private final DurationInspector durationInspector;

    @Override
    public boolean isSuitable(Account account) {
        LocalDateTime lastBalanceUpdateTime = balanceDao.getUpdateTimeByAccount(account.getId());
        log.info("Last balance update time {} for account number {}", lastBalanceUpdateTime, account.getNumber());
        long renewalDuration = MINUTES.between(lastBalanceUpdateTime, LocalDateTime.now().truncatedTo(MINUTES));
        log.info("Duration until last balance update is {} minutes for account number {}",
                renewalDuration, account.getNumber());
        return durationInspector.isValid(renewalDuration);
    }

}
