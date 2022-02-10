package dev.vality.scrooge.service.converter;

import dev.vality.account_balance.BalanceResponse;
import dev.vality.scrooge.domain.BalanceInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BalanceResponseToBalanceInfoConverter implements Converter<BalanceResponse, BalanceInfo> {
    @Override
    public BalanceInfo convert(BalanceResponse source) {

        // TODO impl
        return null;
    }
}
