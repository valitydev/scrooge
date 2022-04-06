package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqTest
@ContextConfiguration(classes = {AccountDaoImpl.class})
class AccountDaoImplTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
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

        Account savedAccount = accountDao.save(account);

        assertNotNull(savedAccount.getId());
        assertEquals(1, dslContext.fetchCount(ACCOUNT));
    }

    @Test
    void saveSameAccountTwice() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());

        Account savedAccount = accountDao.save(account);

        savedAccount.setCurrency("USD");

        Account newSavedAccount = accountDao.save(savedAccount);

        assertEquals(savedAccount.getId(), newSavedAccount.getId());
        assertEquals(1, dslContext.fetchCount(ACCOUNT));
    }
}