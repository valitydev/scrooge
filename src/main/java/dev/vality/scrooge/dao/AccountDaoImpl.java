package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;

@Component
public class AccountDaoImpl extends AbstractDao implements AccountDao {

    public AccountDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Account account) {
        Query query = getDslContext().insertInto(ACCOUNT)
                .set(getDslContext().newRecord(ACCOUNT, account))
                .onConflict(ACCOUNT.ID)
                .doUpdate()
                .set(getDslContext().newRecord(ACCOUNT, account));
        execute(query);
    }
}
