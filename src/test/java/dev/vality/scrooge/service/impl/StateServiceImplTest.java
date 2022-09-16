package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.*;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.scrooge.service.StateService;
import dev.vality.scrooge.service.converter.*;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Option.OPTION;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static dev.vality.scrooge.dao.domain.tables.Terminal.TERMINAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@PostgresqlJooqTest
@ContextConfiguration(classes = {StateServiceImpl.class, BalanceDaoImpl.class, AccountDaoImpl.class,
        ProviderDaoImpl.class, AdapterDaoImpl.class, TerminalDaoImpl.class,
        OptionDaoImpl.class, AdapterInfoToOptionConverter.class, AdapterInfoToAdapterConverter.class,
        BalanceInfoToAccountConverter.class, BalanceInfoToBalanceConverter.class,
        ProviderInfoToProviderConverter.class, TerminalInfoToTerminalConverter.class})
class StateServiceImplTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private StateService stateService;

    @MockBean
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        when(encryptionService.encrypt(anyString())).thenReturn(TestObjectFactory.randomString());
    }

    @Test
    void update() {
        var routeInfo = TestObjectFactory.testRouteInfo();
        BalanceInfo balanceInfo = TestObjectFactory.testBalanceInfo();

        stateService.update(routeInfo, balanceInfo);

        assertEquals(1, dslContext.fetchCount(PROVIDER));
        assertEquals(1, dslContext.fetchCount(ACCOUNT));
        assertEquals(1, dslContext.fetchCount(BALANCE));
        assertEquals(1, dslContext.fetchCount(ADAPTER));
        assertEquals(1, dslContext.fetchCount(TERMINAL));
        assertEquals(1, dslContext.fetchCount(OPTION));


    }
}