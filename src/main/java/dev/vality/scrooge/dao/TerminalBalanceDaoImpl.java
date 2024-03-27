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

    private final TerminalBalanceMapper terminalBalanceMapper;

    public TerminalBalanceDaoImpl(DataSource dataSource, TerminalBalanceMapper terminalBalanceMapper) {
        super(dataSource);
        this.terminalBalanceMapper = terminalBalanceMapper;
    }

    @Override
    public List<TerminalBalance> getAllTerminalBalances() {
        Query query = getDslContext().select(PROVIDER.ID, PROVIDER.NAME, ACCOUNT.ID, ACCOUNT.CURRENCY,
                        TERMINAL.ID, TERMINAL.NAME, BALANCE.VALUE, BALANCE.TIMESTAMP)
                .from(PROVIDER
                        .leftJoin(ACCOUNT).on(PROVIDER.ID.eq(ACCOUNT.PROVIDER_ID))
                        .leftJoin(TERMINAL).on(PROVIDER.ID.eq(TERMINAL.PROVIDER_ID))
                        .leftJoin(BALANCE).on(ACCOUNT.ID.eq(BALANCE.ACCOUNT_ID)));
        return fetch(query, terminalBalanceMapper);
    }


}
