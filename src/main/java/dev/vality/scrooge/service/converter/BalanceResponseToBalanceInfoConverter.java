package dev.vality.scrooge.service.converter;

import dev.vality.account_balance.AccountReference;
import dev.vality.account_balance.Balance;
import dev.vality.account_balance.BalanceResponse;
import dev.vality.scrooge.domain.BalanceInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        LocalDateTime timestamp = Optional.ofNullable(source.getResponseTime())
                .map(s -> LocalDateTime.parse(s, DateTimeFormatter.ISO_INSTANT))
                .map(time -> time.truncatedTo(ChronoUnit.SECONDS))
                .orElse(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        balanceInfo.setTimestamp(timestamp);
        return balanceInfo;
    }
}
