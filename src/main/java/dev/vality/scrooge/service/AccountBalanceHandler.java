package dev.vality.scrooge.service;

import dev.vality.fistful.fistful_stat.InvalidRequest;
import dev.vality.scrooge.account.AccountBalanceResponse;
import dev.vality.scrooge.account.AccountServiceSrv;
import dev.vality.scrooge.dao.AccountBalanceDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBalanceHandler implements AccountServiceSrv.Iface {

    private final AccountBalanceDao accountBalanceDao;

    @Override
    public AccountBalanceResponse getAccountBalances() throws TException {
        log.info("New account balance request");
        try {
            var balances = accountBalanceDao.getAllAccountBalances();
            var response = new AccountBalanceResponse(balances);
            log.debug("Account balance response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Failed to process stat request", e);
            throw new InvalidRequest(List.of(e.getMessage()));
        }
    }
}
