package dev.vality.scrooge.service.impl;

import dev.vality.fistful.base.EventRange;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.exception.FistfulException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void handleWithException() throws TException {
        var machineEvent = TestObjectFactory.testMachineEvent();
        machineEvent.setData(null);

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, never()).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, never()).update(any(WithdrawalTransaction.class));
    }

    @Test
    void handleWithoutStatusChangeEvent() throws TException {
        var machineEvent = TestObjectFactory.testMachineEvent();
        machineEvent.setData(null);

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, never()).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, never()).update(any(WithdrawalTransaction.class));
    }


    @Test
    void handleWithAnotherChangeEvent() throws TException {
        var machineEvent = TestObjectFactory.testMachineLimitCheckEvent();

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, never()).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, never()).update(any(WithdrawalTransaction.class));
    }

    @Test
    void handleWithFistfulException() throws TException {
        var machineEvent = TestObjectFactory.testMachineEvent();
        when(fistfulClient.get(anyString(), any(EventRange.class))).thenThrow(new TException("Error call"));

        FistfulException fistfulException =
                assertThrows(FistfulException.class, () -> eventService.handle(List.of(machineEvent)));

        assertEquals("WithdrawalTransactionService error call fistful: ", fistfulException.getMessage());
    }

    @Test
    void handleOk() throws TException {
        var machineEvent = TestObjectFactory.testMachineEvent();
        var withdrawalState = TestObjectFactory.testWithdrawalState();
        when(fistfulClient.get(anyString(), any(EventRange.class))).thenReturn(withdrawalState);
        var expectedTransaction = TestObjectFactory.testWithdrawalTransactionFromState(withdrawalState);
        doNothing().when(balanceService).update(expectedTransaction);

        eventService.handle(List.of(machineEvent));

        verify(fistfulClient, times(1)).get(machineEvent.getSourceId(), new EventRange());
        verify(balanceService, times(1)).update(expectedTransaction);
    }
}