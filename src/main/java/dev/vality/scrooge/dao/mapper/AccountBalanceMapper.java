package dev.vality.scrooge.dao.mapper;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.scrooge.account.AccountBalance;
import dev.vality.scrooge.account.Balance;
import dev.vality.scrooge.account.Provider;
import dev.vality.scrooge.account.Terminal;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static dev.vality.scrooge.dao.AccountBalanceDaoImpl.TERMINAL_ID;
import static dev.vality.scrooge.dao.AccountBalanceDaoImpl.TERMINAL_NAME;
import static dev.vality.scrooge.dao.domain.Tables.*;

@Component
public class AccountBalanceMapper implements RowMapper<AccountBalance> {
    @Override
    public AccountBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        var terminal = new Terminal()
                .setId(rs.getString(TERMINAL_ID))
                .setName(rs.getString(TERMINAL_NAME));
        var provider = new Provider()
                .setId(rs.getString(PROVIDER.ID.getName()))
                .setName(rs.getString(PROVIDER.NAME.getName()));

        var currencyCode = rs.getString(ACCOUNT.CURRENCY.getName());
        var balance = Optional.ofNullable(rs.getString(BALANCE.VALUE.getName())).map(amount ->
                new Balance().setAmount(getAmount(amount)).setCurrencyCode(currencyCode)).orElse(null);


        return new AccountBalance()
                .setAccountId(rs.getString(ACCOUNT.NUMBER.getName()))
                .setLastUpdated(
                        Optional.ofNullable(rs.getObject(BALANCE.TIMESTAMP.getName(), LocalDateTime.class))
                                .map(TypeUtil::temporalToString)
                                .orElse(null))
                .setTerminal(terminal)
                .setBalance(balance)
                .setProvider(provider);
    }

    private long getAmount(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
