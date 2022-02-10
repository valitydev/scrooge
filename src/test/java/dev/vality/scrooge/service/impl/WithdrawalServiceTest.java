package dev.vality.scrooge.service.impl;

import dev.vality.fistful.base.EventRange;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.fistful.withdrawal.WithdrawalState;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.EventService;
import dev.vality.scrooge.service.converter.MachineEventToTimestampedChangeConverter;
import dev.vality.scrooge.service.converter.WithdrawalStateToWithdrawalTransactionConverter;
import dev.vality.scrooge.service.serde.WithdrawalChangeDeserializer;
import dev.vality.scrooge.service.serde.WithdrawalChangeMachineEventParser;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WithdrawalService.class, WithdrawalChangeDeserializer.class,
        WithdrawalChangeMachineEventParser.class, MachineEventToTimestampedChangeConverter.class,
        WithdrawalStateToWithdrawalTransactionConverter.class, WithdrawalTransactionService.class})
class WithdrawalServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private BalanceService<WithdrawalTransaction> balanceService;

    @MockBean
    private ManagementSrv.Iface fistfulClient;

    @Test
    void handleWithoutStatusChangeEvent() throws TException {
        MachineEvent machineEvent = TestObjectFactory.testMachineEvent();
        machineEvent.setData(null);

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, never()).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, never()).update(any(WithdrawalTransaction.class));
    }

    @Test
    void handleOk() throws TException {
        MachineEvent machineEvent = TestObjectFactory.testMachineEvent();
        WithdrawalState withdrawalState = TestObjectFactory.testWithdrawalState();
        when(fistfulClient.get(anyString(), any(EventRange.class))).thenReturn(withdrawalState);
        WithdrawalTransaction expectedTransaction = TestObjectFactory.testWithdrawalTransaction(withdrawalState);
        doNothing().when(balanceService).update(expectedTransaction);

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, times(1)).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, times(1)).update(expectedTransaction);
    }
}