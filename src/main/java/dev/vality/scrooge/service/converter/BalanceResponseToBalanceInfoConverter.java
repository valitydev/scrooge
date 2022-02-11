package dev.vality.scrooge.service.converter;

import dev.vality.account_balance.AccountReference;
import dev.vality.account_balance.Balance;
import dev.vality.account_balance.BalanceResponse;
import dev.vality.scrooge.domain.BalanceInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class BalanceResponseToBalanceInfoConverter implements Converter<BalanceResponse, BalanceInfo> {

    @Override
    public BalanceInfo convert(BalanceResponse source) {
        BalanceInfo balanceInfo = new BalanceInfo();
        Balance balance = source.getBalance();
        balanceInfo.setAmount(balance.getAmount());
        balanceInfo.setCurrency(balance.getCurrencyCode());
        AccountReference accountReference = source.getAccountReference();
        balanceInfo.setAccountId(accountReference.getId());
        Instant timestamp = Optional.ofNullable(source.getResponseTime())
                .map(Instant::parse)
                .orElse(Instant.now());
        balanceInfo.setTimestamp(timestamp);
        return balanceInfo;
    }
}
