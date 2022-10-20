package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.*;
import dev.vality.scrooge.dao.domain.tables.records.AccountRecord;
import dev.vality.scrooge.dao.domain.tables.records.AdapterRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import dev.vality.scrooge.dao.domain.tables.records.TerminalRecord;
import dev.vality.scrooge.domain.AccountInfo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static dev.vality.scrooge.dao.domain.tables.Account.ACCOUNT;
import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Option.OPTION;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static dev.vality.scrooge.dao.domain.tables.Terminal.TERMINAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PostgresqlJooqTest
@ContextConfiguration(classes = {OptionDaoImpl.class})
class OptionDaoImplTest {

    @Autowired
    private OptionDao optionDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(OPTION).execute();
        dslContext.deleteFrom(ADAPTER).execute();
        dslContext.deleteFrom(PROVIDER).execute();
    }

    @Test
    void saveAll() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        AdapterRecord savedAdapter = dslContext.fetchAny(ADAPTER);
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        int optionCount = 3;
        List<Option> options = TestObjectFactory.testOptions(optionCount, savedAdapter.getId(), savedAccount.getId());

        optionDao.saveAll(options);

        assertEquals(optionCount, dslContext.fetchCount(OPTION));
    }

    @Test
    void saveAllOptionForSameAdapterTwice() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        AdapterRecord savedAdapter = dslContext.fetchAny(ADAPTER);
        Account account = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .execute();
        AccountRecord savedAccount = dslContext.fetchAny(ACCOUNT);
        int optionCount = 3;
        List<Option> options = TestObjectFactory.testOptions(optionCount, savedAdapter.getId(), savedAccount.getId());

        optionDao.saveAll(options);
        List<Option> newOptions =
                List.of(options.get(0),
                        TestObjectFactory.testOption(savedAdapter.getId(), savedAccount.getId()));
        optionDao.saveAll(newOptions);

        assertEquals(optionCount + 1, dslContext.fetchCount(OPTION));
    }

    @Test
    void getAllByAccount() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Account account = TestObjectFactory.testAccount();
        Account secondAccount = TestObjectFactory.testAccount();
        account.setProviderId(savedProvider.getId());
        secondAccount.setProviderId(savedProvider.getId());
        dslContext.insertInto(ACCOUNT)
                .set(dslContext.newRecord(ACCOUNT, account))
                .newRecord()
                .set(dslContext.newRecord(ACCOUNT, secondAccount))
                .execute();
        Terminal terminal = TestObjectFactory.testTerminal();
        terminal.setProviderId(savedProvider.getId());
        dslContext.insertInto(TERMINAL)
                .set(dslContext.newRecord(TERMINAL, terminal))
                .execute();
        TerminalRecord savedTerminal = dslContext.fetchAny(TERMINAL);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .execute();
        AdapterRecord savedAdapter = dslContext.fetchAny(ADAPTER);
        int optionCount = 3;
        Result<Record> accounts = dslContext.select().from(ACCOUNT).fetch();
        List<Option> options = TestObjectFactory.testOptions(optionCount,
                savedAdapter.getId(), accounts.get(0).get(ACCOUNT.ID));
        options.get(0).setTerminalRef(savedTerminal.getTerminalRef());
        options.get(1).setTerminalRef(savedTerminal.getTerminalRef());
        List<Option> optionsForSecondAccount =
                TestObjectFactory.testOptions(optionCount, savedAdapter.getId(), accounts.get(1).get(ACCOUNT.ID));
        optionsForSecondAccount.get(0).setTerminalRef(savedTerminal.getTerminalRef());
        optionsForSecondAccount.get(1).setTerminalRef(savedTerminal.getTerminalRef());
        dslContext.insertInto(OPTION)
                .set(dslContext.newRecord(OPTION, options.get(0)))
                .newRecord()
                .set(dslContext.newRecord(OPTION, options.get(1)))
                .newRecord()
                .set(dslContext.newRecord(OPTION, options.get(2)))
                .newRecord()
                .set(dslContext.newRecord(OPTION, optionsForSecondAccount.get(0)))
                .newRecord()
                .set(dslContext.newRecord(OPTION, optionsForSecondAccount.get(1)))
                .newRecord()
                .set(dslContext.newRecord(OPTION, optionsForSecondAccount.get(2)))
                .execute();

        AccountInfo accountInfo = AccountInfo.builder()
                .accountId(accounts.get(0).get(ACCOUNT.ID))
                .adapterId(savedAdapter.getId())
                .termRef(savedTerminal.getTerminalRef())
                .build();

        List<Option> resultOptions = optionDao.getAllByAccount(accountInfo);

        assertEquals(optionCount - 1, resultOptions.size());
        assertTrue(resultOptions.stream()
                .allMatch(option -> option.getAccountId().equals(accounts.get(0).get(ACCOUNT.ID))));
        assertTrue(resultOptions.stream()
                .allMatch(option -> option.getAdapterId().equals(savedAdapter.getId())));
        assertTrue(resultOptions.stream()
                .allMatch(option -> option.getTerminalRef().equals(savedTerminal.getTerminalRef())));
    }
}