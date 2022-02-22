package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.domain.BalanceInfo;
import org.springframework.stereotype.Component;

@Component
public class BalanceInfoToBalanceConverter {

    public Balance convert(BalanceInfo source, Long accountId) {
        Balance balance = new Balance();
        balance.setValue(String.valueOf(source.getAmount()));
        balance.setTimestamp(source.getTimestamp());
        balance.setAccountId(accountId);
        return balance;
    }
}
