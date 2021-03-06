package dev.vality.scrooge.service.impl;

import dev.vality.damsel.domain.TerminalRef;
import dev.vality.damsel.payment_processing.PartyManagementSrv;
import dev.vality.damsel.payment_processing.Varset;
import dev.vality.scrooge.AccountServiceSrv;
import dev.vality.scrooge.BalanceRequest;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.exception.PartyManagementException;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.ClientBuilder;
import dev.vality.scrooge.service.StateService;
import dev.vality.scrooge.service.converter.BalanceResponseToBalanceInfoConverter;
import dev.vality.scrooge.service.converter.ProviderTerminalToRouteInfoConverter;
import dev.vality.woody.api.flow.error.WUndefinedResultException;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WithdrawalBalanceService.class, AccountSurveyServiceImpl.class,
        BalanceResponseToBalanceInfoConverter.class, WithdrawalRouteService.class,
        ProviderTerminalToRouteInfoConverter.class, UrlInspector.class})
@TestPropertySource(properties = {
        "adapter-client.hosts=adapter-paybox,proxy-mocketbank,adapter-onevision-payout"})
class WithdrawalBalanceServiceTest {

    @Autowired
    private BalanceService<WithdrawalTransaction> balanceService;

    @MockBean
    private ClientBuilder<AccountServiceSrv.Iface> clientBuilder;

    @MockBean
    private StateService stateService;

    @MockBean
    private PartyManagementSrv.Iface partyManagementClient;

    private AccountServiceSrv.Iface accountService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountServiceSrv.Iface.class);
    }

    @Test
    void updateWithPartyManagementException() throws TException {
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenThrow(new TException("Error call"));
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        PartyManagementException partyManagementException =
                assertThrows(PartyManagementException.class, () -> balanceService.update(transaction));

        assertEquals("WithdrawalRouteService error call party-management: ", partyManagementException.getMessage());
    }

    @Test
    void updateWithIncorrectUrl() throws TException {
        var providerTerminal = TestObjectFactory.testProviderTerminal();
        String incorrectUrl = "test";
        providerTerminal.getProxy().setUrl(incorrectUrl);
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenReturn(providerTerminal);
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> balanceService.update(transaction));

        assertEquals("Invalid url for adapter " + incorrectUrl, exception.getMessage());
    }

    @Test
    void updateWithNotAvailableUrl() throws TException {
        var providerTerminal = TestObjectFactory.testProviderTerminal();
        providerTerminal.getProxy().setUrl("http://test:8022/v1");
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenReturn(providerTerminal);
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        balanceService.update(transaction);

        verify(clientBuilder, never()).build(anyString());
        verify(accountService, never()).getBalance(any(BalanceRequest.class));
    }

    @Test
    void updateWithAdapterException() throws TException {
        var providerTerminal = TestObjectFactory.testProviderTerminal();
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenReturn(providerTerminal);
        when(clientBuilder.build(providerTerminal.getProxy().getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class))).thenThrow(new TException("Error call"));
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        balanceService.update(transaction);

        verify(partyManagementClient, times(1))
                .computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class));
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        verify(stateService, times(0)).update(any(RouteInfo.class), any(BalanceInfo.class));
    }

    @Test
    void updateWithAdapterWoodyException() throws TException {
        var providerTerminal = TestObjectFactory.testProviderTerminal();
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenReturn(providerTerminal);
        when(clientBuilder.build(providerTerminal.getProxy().getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class)))
                .thenThrow(new WUndefinedResultException("Error call"));
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        balanceService.update(transaction);

        verify(partyManagementClient, times(1))
                .computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class));
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        verify(stateService, times(0)).update(any(RouteInfo.class), any(BalanceInfo.class));
    }

    @Test
    void update() throws TException {
        var providerTerminal = TestObjectFactory.testProviderTerminal();
        var balanceResponse = TestObjectFactory.testBalanceResponse();
        when(partyManagementClient.computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class)))
                .thenReturn(providerTerminal);
        when(clientBuilder.build(providerTerminal.getProxy().getUrl())).thenReturn(accountService);
        when(accountService.getBalance(any(BalanceRequest.class))).thenReturn(balanceResponse);
        doNothing().when(stateService).update(any(RouteInfo.class), any(BalanceInfo.class));
        var transaction = TestObjectFactory.testWithdrawalTransaction();

        balanceService.update(transaction);

        verify(partyManagementClient, times(1))
                .computeProviderTerminal(any(TerminalRef.class), anyLong(), any(Varset.class));
        verify(accountService, times(1)).getBalance(any(BalanceRequest.class));
        verify(stateService, times(1)).update(any(RouteInfo.class), any(BalanceInfo.class));
    }
}