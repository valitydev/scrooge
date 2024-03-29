package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.TerminalBalanceDao;
import dev.vality.scrooge.service.TerminalBalanceHandler;
import dev.vality.scrooge.terminal.balance.TerminalBalance;
import dev.vality.scrooge.terminal.balance.TerminalServiceSrv;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TerminalBalanceHandler.class})
public class TerminalBalanceHandlerTest {

    @Autowired
    private TerminalServiceSrv.Iface requestHandler;

    @MockBean
    private TerminalBalanceDao terminalBalanceDao;

    @Test
    void getTerminalBalancesTest() throws TException {
        Mockito.when(terminalBalanceDao.getAllTerminalBalances()).thenReturn(List.of(new TerminalBalance()));
        var result = requestHandler.getTerminalBalances();
        assertNotNull(result);
        assertNotNull(result.getBalances());
        assertEquals(1, result.getBalances().size());
    }
}
