package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.mapper.TerminalBalanceMapper;
import dev.vality.scrooge.terminal.balance.TerminalBalance;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static dev.vality.scrooge.dao.domain.Tables.*;


@Component
public class TerminalBalanceDaoImpl extends AbstractDao implements TerminalBalanceDao {

    public final static String ACCOUNT_ID = "ACCOUNT_ID";
    public final static String TERMINAL_ID = "TERMINAL_ID";
    public final static String TERMINAL_NAME = "TERMINAL_NAME";

    private final TerminalBalanceMapper terminalBalanceMapper;

    public TerminalBalanceDaoImpl(DataSource dataSource, TerminalBalanceMapper terminalBalanceMapper) {
        super(dataSource);
        this.terminalBalanceMapper = terminalBalanceMapper;
    }

    @Override
    public List<TerminalBalance> getAllTerminalBalances() {
        Query query = getDslContext().select(PROVIDER.ID, PROVIDER.NAME, ACCOUNT.ID.as(ACCOUNT_ID), ACCOUNT.CURRENCY,
                        ACCOUNT.NUMBER, TERMINAL.ID.as(TERMINAL_ID), TERMINAL.NAME.as(TERMINAL_NAME),
                        BALANCE.VALUE, BALANCE.TIMESTAMP)
                .from(TERMINAL
                        .leftJoin(PROVIDER).on(PROVIDER.ID.eq(TERMINAL.PROVIDER_ID))
                        .leftJoin(ACCOUNT).on(PROVIDER.ID.eq(ACCOUNT.PROVIDER_ID))
                        .leftJoin(BALANCE).on(ACCOUNT.ID.eq(BALANCE.ACCOUNT_ID)))
                .orderBy(PROVIDER.ID.desc());
        return fetch(query, terminalBalanceMapper);
    }


}
