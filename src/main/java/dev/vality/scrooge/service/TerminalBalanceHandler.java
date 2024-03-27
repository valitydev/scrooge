package dev.vality.scrooge.service;

import dev.vality.fistful.fistful_stat.InvalidRequest;
import dev.vality.scrooge.dao.TerminalBalanceDao;
import dev.vality.scrooge.terminal.balance.TerminalBalanceResponse;
import dev.vality.scrooge.terminal.balance.TerminalServiceSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalBalanceHandler implements TerminalServiceSrv.Iface {

    private final TerminalBalanceDao terminalBalanceDao;

    @Override
    public TerminalBalanceResponse getTerminalBalances() throws TException {
        log.info("New terminal balance request");
        try {
            var balances = terminalBalanceDao.getAllTerminalBalances();
            var response = new TerminalBalanceResponse(balances);
            log.debug("Terminal balance response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Failed to process stat request", e);
            throw new InvalidRequest(List.of(e.getMessage()));
        }
    }
}
