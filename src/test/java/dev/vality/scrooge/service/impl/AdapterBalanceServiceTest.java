package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.AccountDaoImpl;
import dev.vality.scrooge.dao.BalanceDaoImpl;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import dev.vality.scrooge.dao.domain.tables.records.BalanceRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.scrooge.service.converter.BalanceInfoToAccountConverter;
import dev.vality.scrooge.service.converter.BalanceInfoToBalanceConverter;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@PostgresqlJooqTest
@ContextConfiguration(classes = {AdapterBalanceService.class, BalanceDaoImpl.class,
        AccountDaoImpl.class, BalanceInfoToAccountConverter.class,
        BalanceInfoToBalanceConverter.class})
class AdapterBalanceServiceTest {

    @Autowired
    private BalanceService<AdapterInfo> adapterBalanceService;

    @MockitoBean
    private AccountSurveyService accountSurveyService;

    @Autowired
    private DSLContext dslContext;

    @MockitoBean
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        when(encryptionService.encrypt(anyString())).thenReturn(TestObjectFactory.randomString());
    }

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
        when(accountSurveyService.getBalance(any(AdapterInfo.class))).thenReturn(null);
        AdapterInfo adapterInfo = TestObjectFactory.testAdapterInfo();
        adapterInfo.setProviderId(savedProvider.getId());

        adapterBalanceService.update(adapterInfo);

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
        BalanceInfo balanceInfo = TestObjectFactory.testBalanceInfo();
        balanceInfo.setAccountId(account.getNumber());
        AdapterInfo adapterInfo = TestObjectFactory.testAdapterInfo();
        adapterInfo.setProviderId(savedProvider.getId());
        when(accountSurveyService.getBalance(adapterInfo)).thenReturn(balanceInfo);

        adapterBalanceService.update(adapterInfo);

        BalanceRecord updatedBalance = dslContext.fetchAny(BALANCE);
        assertEquals(balanceInfo.getAmount(), Long.valueOf(updatedBalance.getValue()));


    }
}