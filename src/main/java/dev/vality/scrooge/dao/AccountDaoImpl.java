package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;

@Component
public class AccountDaoImpl extends AbstractDao implements AccountDao {

    public AccountDaoImpl(DataSource dataSource) {
        super(dataSource);
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
}
