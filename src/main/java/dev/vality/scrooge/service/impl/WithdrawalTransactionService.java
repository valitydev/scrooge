package dev.vality.scrooge.service.impl;

import dev.vality.fistful.base.EventRange;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.fistful.withdrawal.WithdrawalState;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.exception.FistfulException;
import dev.vality.scrooge.service.TransactionService;
import dev.vality.scrooge.service.converter.WithdrawalStateToWithdrawalTransactionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalTransactionService implements TransactionService<WithdrawalTransaction> {

    private final ManagementSrv.Iface fistfulClient;
    private final WithdrawalStateToWithdrawalTransactionConverter converter;

    @Override
    public WithdrawalTransaction getInfo(String id) {
        try {
            log.info("Try to get withdrawal transaction info  for withdrawalId {}", id);
            WithdrawalState withdrawalState = fistfulClient.get(id, new EventRange());
            WithdrawalTransaction transaction = converter.convert(withdrawalState);
            log.info("Success response for withdrawalId {} ,  transaction: {} ", id, transaction);
            return transaction;
        } catch (TException e) {
            throw new FistfulException("WithdrawalTransactionService error call fistful: ", e);
        }
    }
}
