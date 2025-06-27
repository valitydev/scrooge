package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.account.AccountBalance;
import dev.vality.scrooge.dao.AccountBalanceDao;
import dev.vality.scrooge.service.AccountBalanceHandler;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AccountBalanceHandler.class})
public class AccountBalanceHandlerTest {

    @Autowired
    private AccountBalanceHandler requestHandler;

    @MockitoBean
    private AccountBalanceDao accountBalanceDao;

    @Test
    void getTerminalBalancesTest() throws TException {
        Mockito.when(accountBalanceDao.getAllAccountBalances()).thenReturn(List.of(new AccountBalance()));
        var result = requestHandler.getAccountBalances();
        assertNotNull(result);
        assertNotNull(result.getBalances());
        assertEquals(1, result.getBalances().size());
    }
}
