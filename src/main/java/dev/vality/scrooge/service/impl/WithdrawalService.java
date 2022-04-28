package dev.vality.scrooge.service.impl;

import dev.vality.fistful.withdrawal.Change;
import dev.vality.fistful.withdrawal.StatusChange;
import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.fistful.withdrawal.status.Failed;
import dev.vality.fistful.withdrawal.status.Status;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.EventService;
import dev.vality.scrooge.service.TransactionService;
import dev.vality.scrooge.service.converter.MachineEventToTimestampedChangeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalService implements EventService {

    private final MachineEventToTimestampedChangeConverter converter;
    private final TransactionService<WithdrawalTransaction> transactionService;
    private final BalanceService<WithdrawalTransaction> balanceService;

    @Override
    public void handle(List<MachineEvent> events) {
        for (MachineEvent event : events) {
            processEvent(event);
        }
    }

    private void processEvent(MachineEvent event) {
        Status status = getStatus(event);
        if (status.isSetSucceeded()) {
            long sequenceId = event.getEventId();
            String withdrawalId = event.getSourceId();
            log.info("WithdrawalService get success withdrawal status changed, sequenceId={}, withdrawalId={}",
                    sequenceId, withdrawalId);
            WithdrawalTransaction transactionInfo = transactionService.getInfo(withdrawalId);
            balanceService.update(transactionInfo);
        }
    }

    private Status getStatus(MachineEvent event) {
        return Optional.ofNullable(converter.convert(event))
                .map(TimestampedChange::getChange)
                .filter(Change::isSetStatusChanged)
                .map(Change::getStatusChanged)
                .map(StatusChange::getStatus)
                .orElse(Status.failed(new Failed()));
    }
}
