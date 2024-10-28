package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.domain.BalanceInfo;
import org.springframework.stereotype.Component;

@Component
public class BalanceInfoToAccountConverter {

    public Account convert(BalanceInfo source, Integer providerId) {
        Account account = new Account();
        account.setCurrency(source.getCurrency());
        account.setNumber(source.getAccountId());
        account.setProviderId(providerId);
        account.setActive(Boolean.TRUE);
        return account;
    }
}
