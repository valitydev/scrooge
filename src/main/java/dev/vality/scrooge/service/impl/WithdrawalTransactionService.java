package dev.vality.scrooge.service.impl;

import dev.vality.fistful.base.EventRange;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.fistful.withdrawal.WithdrawalState;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.exception.FistfulException;
import dev.vality.scrooge.service.TransactionService;
import dev.vality.scrooge.service.converter.WithdrawalStateToWithdrawalTransactionConverter;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalTransactionService implements TransactionService<WithdrawalTransaction> {

    private final ManagementSrv.Iface fistfulClient;
    private final WithdrawalStateToWithdrawalTransactionConverter converter;

    @Override
    public WithdrawalTransaction getInfo(String id) {
        try {
            EventRange eventRange = new EventRange(); // TODO is fulfilment mandatory?
            WithdrawalState withdrawalState = fistfulClient.get(id, eventRange);
            return converter.convert(withdrawalState);
        } catch (TException e) {
            throw new FistfulException("WithdrawalTransactionService error call fistful: ", e);
        }
    }
}
