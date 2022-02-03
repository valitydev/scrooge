package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;

@Component
public class BalanceDaoImpl extends AbstractDao implements BalanceDao {

    public BalanceDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Balance balance) {
        Query query = getDslContext().insertInto(BALANCE)
                .set(getDslContext().newRecord(BALANCE, balance))
                .onConflict(BALANCE.ID)
                .doUpdate()
                .set(getDslContext().newRecord(BALANCE, balance));
        execute(query);
    }
}
