package dev.vality.scrooge.service.impl;

import dev.vality.damsel.domain.TerminalRef;
import dev.vality.damsel.payment_processing.PartyManagementSrv;
import dev.vality.damsel.payment_processing.ProviderTerminal;
import dev.vality.damsel.payment_processing.Varset;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.exception.PartyManagementException;
import dev.vality.scrooge.service.RouteService;
import dev.vality.scrooge.service.converter.ProviderTerminalToRouteInfoConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalRouteService implements RouteService<WithdrawalTransaction> {

    private final PartyManagementSrv.Iface partyManagementClient;
    private final ProviderTerminalToRouteInfoConverter converter;

    @Override
    public RouteInfo get(WithdrawalTransaction transaction) {
        try {
            TerminalRef terminalRef = new TerminalRef();
            terminalRef.setId(transaction.getTerminalId());
            long domainVersionId = transaction.getDomainVersionId();
            ProviderTerminal providerTerminal =
                    partyManagementClient.computeProviderTerminal(terminalRef, domainVersionId, new Varset());
            return converter.convert(providerTerminal);
        } catch (TException e) {
            log.error("WithdrawalRouteService error get terminal with id={}, version={}",
                    transaction.getTerminalId(), transaction.getDomainVersionId());
            throw new PartyManagementException("WithdrawalRouteService error call party-management: ", e);
        }
    }
}
