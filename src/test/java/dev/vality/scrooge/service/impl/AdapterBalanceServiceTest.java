package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.AccountDaoImpl;
import dev.vality.scrooge.dao.AdapterDaoImpl;
import dev.vality.scrooge.dao.BalanceDaoImpl;
import dev.vality.scrooge.dao.OptionDaoImpl;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import dev.vality.scrooge.dao.domain.tables.records.BalanceRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.converter.BalanceInfoToAccountConverter;
import dev.vality.scrooge.service.converter.BalanceInfoToBalanceConverter;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@PostgresqlJooqTest
@ContextConfiguration(classes = {AdapterBalanceService.class, BalanceDaoImpl.class, AdapterDaoImpl.class,
        OptionDaoImpl.class, AccountDaoImpl.class, BalanceInfoToAccountConverter.class,
        BalanceInfoToBalanceConverter.class})
class AdapterBalanceServiceTest {

    @Autowired
    private BalanceService<Adapter> adapterBalanceService;

    @MockBean
    private AccountSurveyService accountSurveyService;

    @Autowired
    private DSLContext dslContext;

    @Test
    void failedUpdate() {
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
        dslContext.insertInto(BALANCE)
                .set(dslContext.newRecord(BALANCE, balance))
                .execute();
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        when(accountSurveyService.getBalance(any(AdapterInfo.class))).thenReturn(null);

        adapterBalanceService.update(adapter);

        BalanceRecord updatedBalance = dslContext.fetchAny(BALANCE);
        assertEquals(balance.getValue(), updatedBalance.getValue());


    }

    @Test
    void successUpdate() {
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
        dslContext.insertInto(BALANCE)
                .set(dslContext.newRecord(BALANCE, balance))
                .execute();
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        BalanceInfo balanceInfo = TestObjectFactory.testBalanceInfo();
        balanceInfo.setAccountId(account.getNumber());
        when(accountSurveyService.getBalance(any(AdapterInfo.class))).thenReturn(balanceInfo);

        adapterBalanceService.update(adapter);

        BalanceRecord updatedBalance = dslContext.fetchAny(BALANCE);
        assertEquals(balanceInfo.getAmount(), Long.valueOf(updatedBalance.getValue()));


    }
}