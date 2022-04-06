package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlJooqTest
@ContextConfiguration(classes = {BalanceDaoImpl.class})
class BalanceDaoImplTest {

    @Autowired
    private BalanceDao balanceDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(BALANCE).execute();
        dslContext.deleteFrom(ACCOUNT).execute();
        dslContext.deleteFrom(PROVIDER).execute();
    }

    @Test
    void save() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        Balance balance = TestObjectFactory.testBalance(savedAccount.getId());

        balanceDao.save(balance);

        assertEquals(1, dslContext.fetchCount(BALANCE));
    }

    @Test
    void saveBalanceForSameAccountTwice() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        Balance balance = TestObjectFactory.testBalance(savedAccount.getId());

        balanceDao.save(balance);

        balance.setValue("1000.00");

        balanceDao.save(balance);

        assertEquals(1, dslContext.fetchCount(BALANCE));
    }
}