package dev.vality.scrooge.dao.mapper;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.scrooge.terminal.balance.Balance;
import dev.vality.scrooge.terminal.balance.Provider;
import dev.vality.scrooge.terminal.balance.Terminal;
import dev.vality.scrooge.terminal.balance.TerminalBalance;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static dev.vality.scrooge.dao.TerminalBalanceDaoImpl.TERMINAL_ID;
import static dev.vality.scrooge.dao.TerminalBalanceDaoImpl.TERMINAL_NAME;
import static dev.vality.scrooge.dao.domain.Tables.*;

@Component
public class TerminalBalanceMapper implements RowMapper<TerminalBalance> {
    @Override
    public TerminalBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
        var terminal = new Terminal()
                .setId(rs.getString(TERMINAL_ID))
                .setName(rs.getString(TERMINAL_NAME));
        var provider = new Provider()
                .setId(rs.getString(PROVIDER.ID.getName()))
                .setName(rs.getString(PROVIDER.NAME.getName()));

        var balance = new Balance()
                .setAmount(rs.getString(BALANCE.VALUE.getName()))
                .setCurrencyCode(rs.getString(ACCOUNT.CURRENCY.getName()));

        return new TerminalBalance()
                .setAccountId(rs.getString(ACCOUNT.NUMBER.getName()))
                .setLastUpdated(
                        Optional.ofNullable(rs.getObject(BALANCE.TIMESTAMP.getName(), LocalDateTime.class))
                                .map(TypeUtil::temporalToString)
                                .orElse(null))
                .setTerminal(terminal)
                .setBalance(balance)
                .setProvider(provider);
    }
}
