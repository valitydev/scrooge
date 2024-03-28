package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.TerminalRecord;
import dev.vality.scrooge.dao.mapper.TerminalBalanceMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Balance.BALANCE;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static dev.vality.scrooge.dao.domain.tables.Terminal.TERMINAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PostgresqlJooqTest
@ContextConfiguration(classes = {
        ProviderDaoImpl.class, TerminalDaoImpl.class, AccountDaoImpl.class,
        BalanceDaoImpl.class, TerminalBalanceDaoImpl.class, TerminalBalanceMapper.class
})
class TerminalBalanceDaoImplTest {

    @Autowired
    private ProviderDao providerDao;

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private BalanceDao balanceDao;

    @Autowired
    private TerminalBalanceDao terminalBalanceDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(TERMINAL).execute();
        dslContext.deleteFrom(PROVIDER).execute();
        dslContext.deleteFrom(BALANCE).execute();
        dslContext.deleteFrom(ACCOUNT).execute();
    }


    @Test
    void mappingTest() {
        final var provider = createProvider();
        final var terminal = createTerminal(provider.getId());
        final var account = createAccount(provider.getId());
        final var balance = createBalance(account.getId());

        var result = terminalBalanceDao.getAllTerminalBalances();
        assertEquals(1, result.size());

        var terminalBalance = result.iterator().next();
        assertEquals(account.getNumber(), terminalBalance.getAccountId());
        assertEquals(terminal.getId().toString(), terminalBalance.getTerminal().getId());
        assertEquals(terminal.getName(), terminalBalance.getTerminal().getName());
        assertEquals(provider.getId().toString(), terminalBalance.getProvider().getId());
        assertEquals(provider.getName(), terminalBalance.getProvider().getName());
        assertEquals(balance.getValue(), terminalBalance.getBalance().getAmount());
        assertEquals(account.getCurrency(), terminalBalance.getBalance().getCurrencyCode());
    }

    @Test
    void orderTest() {
        createTestRow();
        createTestRow();

        var result = terminalBalanceDao.getAllTerminalBalances();
        assertEquals(2, result.size());

        var iterator = result.iterator();
        var terminalBalance1 = iterator.next();
        var terminalBalance2 = iterator.next();
        assertTrue(Long.valueOf(terminalBalance1.getTerminal().getId())
                .compareTo(Long.valueOf(terminalBalance2.getTerminal().getId())) > 0);
    }

    @Test
    void withoutTerminalTest() {
        createTestRow();
        var provider = createProvider();
        var account = createAccount(provider.getId());
        var balance = createBalance(account.getId());

        var result = terminalBalanceDao.getAllTerminalBalances();
        assertEquals(1, result.size());
    }

    @Test
    void npeTest() {
        var provider = createProvider();
        createTerminal(provider.getId());

        var result = terminalBalanceDao.getAllTerminalBalances();
        assertEquals(1, result.size());
    }

    private void createTestRow() {
        var provider = createProvider();
        var terminal = createTerminal(provider.getId());
        var account = createAccount(provider.getId());
        var balance = createBalance(account.getId());
    }

    private Provider createProvider() {
        Provider provider = TestObjectFactory.testProvider();
        return providerDao.save(provider);
    }

    private TerminalRecord createTerminal(Integer providerId) {
        var terminal = TestObjectFactory.testTerminal();
        terminal.setProviderId(providerId);
        terminalDao.save(terminal);
        return dslContext.fetchOne(TERMINAL, TERMINAL.NAME.eq(terminal.getName()));
    }

    private Account createAccount(Integer providerId) {
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(providerId);
        return accountDao.save(account);
    }

    private Balance createBalance(Long accountId) {
        var balance = TestObjectFactory.testBalance(accountId);
        balanceDao.save(balance);
        return balance;
    }
}