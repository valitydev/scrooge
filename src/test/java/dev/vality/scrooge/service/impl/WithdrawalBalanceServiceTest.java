package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.account_balance.BalanceRequest;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.AccountDaoImpl;
import dev.vality.scrooge.dao.BalanceDaoImpl;
import dev.vality.scrooge.dao.ProviderDaoImpl;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.scrooge.service.RouteService;
import dev.vality.scrooge.service.converter.BalanceResponseToBalanceInfoConverter;
import org.apache.thrift.TException;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@PostgresqlJooqTest
@ContextConfiguration(classes = {WithdrawalBalanceService.class, BalanceDaoImpl.class, AccountDaoImpl.class,
        ProviderDaoImpl.class, WithdrawalRouteService.class, AccountSurveyServiceImpl.class,
        BalanceResponseToBalanceInfoConverter.class})
class WithdrawalBalanceServiceTest {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private BalanceService<WithdrawalTransaction> balanceService;

    @MockBean
    private ClientBuilder<AccountServiceSrv.Iface> clientBuilder;

    @MockBean
    private RouteService<WithdrawalTransaction> routeService;

    private AccountServiceSrv.Iface accountService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountServiceSrv.Iface.class);
    }

    @Test
    void update() throws TException {
        var transaction = TestObjectFactory.testWithdrawalTransaction();
        var routeInfo = TestObjectFactory.testRouteInfo();
        var balanceResponse = TestObjectFactory.testBalanceResponse();
        when(routeService.get(transaction)).thenReturn(routeInfo);
        when(clientBuilder.build(routeInfo.getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class))).thenReturn(balanceResponse);

        balanceService.update(transaction);

        verify(routeService, times(1)).get(transaction);
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        assertEquals(1, dslContext.fetchCount(PROVIDER));
        assertEquals(1, dslContext.fetchCount(ACCOUNT));
        assertEquals(1, dslContext.fetchCount(BALANCE));
    }
}