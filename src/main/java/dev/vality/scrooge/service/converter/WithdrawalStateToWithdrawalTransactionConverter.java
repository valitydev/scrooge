package dev.vality.scrooge.service.converter;

import dev.vality.fistful.withdrawal.Route;
import dev.vality.fistful.withdrawal.WithdrawalState;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalStateToWithdrawalTransactionConverter
        implements Converter<WithdrawalState, WithdrawalTransaction> {

    @Override
    public WithdrawalTransaction convert(WithdrawalState source) {
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
        withdrawalTransaction.setWithdrawalId(source.getId());
        withdrawalTransaction.setDomainVersionId(source.getDomainRevision());
        if (source.isSetRoute()) {
            Route route = source.getRoute();
            withdrawalTransaction.setProviderId(route.getProviderId());
            withdrawalTransaction.setTerminalId(route.getTerminalId());
        }
        return withdrawalTransaction;
    }
}
