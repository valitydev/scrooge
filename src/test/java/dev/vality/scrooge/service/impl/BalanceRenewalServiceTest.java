package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.AccountDaoImpl;
import dev.vality.scrooge.dao.AdapterDaoImpl;
import dev.vality.scrooge.dao.BalanceDaoImpl;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.DurationInspector;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AdapterDaoImpl.class, BalanceRenewalService.class, BalanceDaoImpl.class, AccountDaoImpl.class})
@PostgresqlJooqTest
@TestPropertySource(properties = {
        "service.renewal.cron=0/5 * * * *"})
class BalanceRenewalServiceTest {

    @Autowired
    private BalanceRenewalService balanceRenewalService;

    @MockBean
    private BalanceService<Adapter> adapterBalanceService;

    @MockBean
    private DurationInspector durationInspector;

    @Autowired
    private DSLContext dslContext;

    @Test
    void renewWithoutAdapters() {

        balanceRenewalService.renew();

        verify(adapterBalanceService, never()).update(any(Adapter.class));
    }

    @Test
    void notRenewBalanceWithInvalidDuration() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        Balance balance = TestObjectFactory.testBalance(savedAccount.getId());
        dslContext.insertInto(BALANCE)
                .set(dslContext.newRecord(BALANCE, balance))
                .execute();
        when(durationInspector.isValid(anyLong())).thenReturn(false);


        balanceRenewalService.renew();

        verify(adapterBalanceService, times(0)).update(any(Adapter.class));
    }

    @Test
    void successRenewBalance() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        Balance balance = TestObjectFactory.testBalance(savedAccount.getId());
        dslContext.insertInto(BALANCE)
                .set(dslContext.newRecord(BALANCE, balance))
                .execute();
        when(durationInspector.isValid(anyLong())).thenReturn(true);


        balanceRenewalService.renew();

        verify(adapterBalanceService, times(1)).update(any(Adapter.class));
    }
}