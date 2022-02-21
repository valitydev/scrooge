package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.*;
import dev.vality.scrooge.dao.domain.tables.pojos.*;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.service.StateService;
import dev.vality.scrooge.service.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final BalanceDao balanceDao;
    private final AccountDao accountDao;
    private final ProviderDao providerDao;
    private final TerminalDao terminalDao;
    private final AdapterDao adapterDao;
    private final OptionDao optionDao;
    private final ProviderInfoToProviderConverter providerConverter;
    private final TerminalInfoToTerminalConverter terminalConverter;
    private final BalanceInfoToAccountConverter accountConverter;
    private final BalanceInfoToBalanceConverter balanceConverter;
    private final AdapterInfoToAdapterConverter adapterConverter;
    private final AdapterInfoToOptionConverter optionConverter;

    @Override
    public void update(RouteInfo routeInfo, BalanceInfo balanceInfo) {
        log.info("Update state for routeInfo={}, balanceInfo={}", routeInfo, balanceInfo);
        Provider provider = providerConverter.convert(routeInfo.getProviderInfo());
        Provider savedProvider = providerDao.save(provider);
        Terminal terminal = terminalConverter.convert(routeInfo.getTerminalInfo(), savedProvider.getId());
        terminalDao.save(terminal);
        Adapter adapter = adapterConverter.convert(routeInfo.getAdapterInfo(), savedProvider.getId());
        Adapter savedAdapter = adapterDao.save(adapter);
        List<Option> options = optionConverter.convert(routeInfo.getAdapterInfo(), savedAdapter.getId());
        optionDao.saveAll(options);
        Account account = accountConverter.convert(balanceInfo, savedProvider.getId());
        Account savedAccount = accountDao.save(account);
        Balance balance = balanceConverter.convert(balanceInfo, savedAccount.getId());
        balanceDao.save(balance);
    }
}
