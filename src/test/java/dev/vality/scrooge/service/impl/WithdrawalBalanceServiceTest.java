package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import dev.vality.account_balance.BalanceRequest;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.scrooge.service.RouteService;
import dev.vality.scrooge.service.StateService;
import dev.vality.scrooge.service.converter.BalanceResponseToBalanceInfoConverter;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WithdrawalBalanceService.class, AccountSurveyServiceImpl.class,
        BalanceResponseToBalanceInfoConverter.class})
class WithdrawalBalanceServiceTest {

    @Autowired
    private BalanceService<WithdrawalTransaction> balanceService;

    @MockBean
    private ClientBuilder<AccountServiceSrv.Iface> clientBuilder;

    @MockBean
    private StateService stateService;

    @MockBean
    private RouteService<WithdrawalTransaction> routeService;

    private AccountServiceSrv.Iface accountService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountServiceSrv.Iface.class);
    }

    @Test
    void updateFailure() throws TException {
        var transaction = TestObjectFactory.testWithdrawalTransaction();
        var routeInfo = TestObjectFactory.testRouteInfo();
        var balanceResponse = TestObjectFactory.testBalanceResponse();
        when(routeService.get(transaction)).thenReturn(routeInfo);
        when(clientBuilder.build(routeInfo.getAdapterInfo().getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class))).thenThrow(new TException("Error call"));

        balanceService.update(transaction);

        verify(routeService, times(1)).get(transaction);
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        verify(stateService, times(0)).update(any(RouteInfo.class), any(BalanceInfo.class));
    }

    @Test
    void update() throws TException {
        var transaction = TestObjectFactory.testWithdrawalTransaction();
        var routeInfo = TestObjectFactory.testRouteInfo();
        var balanceResponse = TestObjectFactory.testBalanceResponse();
        when(routeService.get(transaction)).thenReturn(routeInfo);
        when(clientBuilder.build(routeInfo.getAdapterInfo().getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class))).thenReturn(balanceResponse);
        doNothing().when(stateService).update(any(RouteInfo.class), any(BalanceInfo.class));

        balanceService.update(transaction);

        verify(routeService, times(1)).get(transaction);
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        verify(stateService, times(1)).update(any(RouteInfo.class), any(BalanceInfo.class));
    }
}