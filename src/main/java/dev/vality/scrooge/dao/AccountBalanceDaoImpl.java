package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.mapper.AccountBalanceMapper;
import dev.vality.scrooge.account.AccountBalance;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static dev.vality.scrooge.dao.domain.Tables.*;


@Component
public class AccountBalanceDaoImpl extends AbstractDao implements AccountBalanceDao {

    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    public static final String TERMINAL_ID = "TERMINAL_ID";
    public static final String TERMINAL_NAME = "TERMINAL_NAME";

    private final AccountBalanceMapper accountBalanceMapper;

    public AccountBalanceDaoImpl(DataSource dataSource, AccountBalanceMapper accountBalanceMapper) {
        super(dataSource);
        this.accountBalanceMapper = accountBalanceMapper;
    }

    @Override
    public List<AccountBalance> getAllAccountBalances() {
        Query query = getDslContext().select(PROVIDER.ID, PROVIDER.NAME, ACCOUNT.ID.as(ACCOUNT_ID), ACCOUNT.CURRENCY,
                        ACCOUNT.NUMBER, TERMINAL.ID.as(TERMINAL_ID), TERMINAL.NAME.as(TERMINAL_NAME),
                        BALANCE.VALUE, BALANCE.TIMESTAMP, PROVIDER.PROVIDER_REF, TERMINAL.TERMINAL_REF)
                .from(TERMINAL
                        .leftJoin(PROVIDER).on(PROVIDER.ID.eq(TERMINAL.PROVIDER_ID))
                        .leftJoin(ACCOUNT).on(PROVIDER.ID.eq(ACCOUNT.PROVIDER_ID))
                        .leftJoin(BALANCE).on(ACCOUNT.ID.eq(BALANCE.ACCOUNT_ID)))
                .where(PROVIDER.ID.isNotNull().and(ACCOUNT.ID.isNotNull()))
                .orderBy(TERMINAL.TERMINAL_REF.desc());
        return fetch(query, accountBalanceMapper);
    }
}
