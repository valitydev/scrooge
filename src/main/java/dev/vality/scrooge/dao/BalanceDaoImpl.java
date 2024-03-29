package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import org.jooq.Query;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
                .onConflict(BALANCE.ACCOUNT_ID)
                .doUpdate()
                .set(getDslContext().newRecord(BALANCE, balance));
        execute(query);
    }

    @Override
    public LocalDateTime getUpdateTimeByAccount(Long accountId) {
        SelectConditionStep<Record1<LocalDateTime>> where = getDslContext().select(BALANCE.TIMESTAMP)
                .from(BALANCE)
                .where(BALANCE.ACCOUNT_ID.eq(accountId));
        LocalDateTime updateTime = fetchOne(where, LocalDateTime.class);
        return updateTime.truncatedTo(ChronoUnit.SECONDS);
    }
}
