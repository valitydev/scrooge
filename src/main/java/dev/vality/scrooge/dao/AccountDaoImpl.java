package dev.vality.scrooge.dao;

import dev.vality.mapper.RecordRowMapper;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import org.jooq.Query;
import org.jooq.SelectConditionStep;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;

@Component
public class AccountDaoImpl extends AbstractDao implements AccountDao {

    private final RowMapper<Account> listRecordRowMapper;

    public AccountDaoImpl(DataSource dataSource) {
        super(dataSource);
        listRecordRowMapper = new RecordRowMapper<>(ACCOUNT, Account.class);
    }

    @Override
    public Account save(Account account) {
        Query query = getDslContext().insertInto(ACCOUNT)
                .set(getDslContext().newRecord(ACCOUNT, account))
                .onConflict(ACCOUNT.NUMBER, ACCOUNT.PROVIDER_ID)
                .doUpdate()
                .set(getDslContext().newRecord(ACCOUNT, account))
                .returning(ACCOUNT.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        account.setId(keyHolder.getKey().longValue());
        return account;
    }

    @Override
    public List<Account> getAllActive() {
        SelectConditionStep<AccountRecord> where = getDslContext()
                .selectFrom(ACCOUNT)
                .where(ACCOUNT.ACTIVE.eq(Boolean.TRUE));
        return fetch(where, listRecordRowMapper);
    }
}
