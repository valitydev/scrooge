package dev.vality.scrooge.dao.mapper;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.scrooge.base.Balance;
import dev.vality.scrooge.terminal.balance.Provider;
import dev.vality.scrooge.terminal.balance.Terminal;
import dev.vality.scrooge.terminal.balance.TerminalBalance;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static dev.vality.scrooge.dao.domain.Tables.*;

@Component
public class TerminalBalanceMapper implements RowMapper<TerminalBalance> {
    @Override
    public TerminalBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        var terminal = new Terminal()
                .setId(rs.getString(TERMINAL.ID.getName()))
                .setName(rs.getString(TERMINAL.NAME.getName()));
        var provider = new Provider()
                .setId(rs.getString(PROVIDER.ID.getName()))
                .setName(rs.getString(PROVIDER.NAME.getName()));

        var balance = new Balance()
                .setAmount(rs.getLong(BALANCE.VALUE.getName()))
                .setCurrencyCode(rs.getString(ACCOUNT.CURRENCY.getName()));

        return new TerminalBalance()
                .setAccountId(rs.getString(ACCOUNT.ID.getName()))
                .setLastUpdated(
                        TypeUtil.temporalToString(rs.getObject(BALANCE.TIMESTAMP.getName(), LocalDateTime.class)))
                .setTerminal(terminal)
                .setBalance(balance)
                .setProvider(provider);
    }
}
