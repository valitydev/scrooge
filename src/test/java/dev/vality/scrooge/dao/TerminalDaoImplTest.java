package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.pojos.Terminal;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static dev.vality.scrooge.dao.domain.tables.Terminal.TERMINAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlJooqTest
@ContextConfiguration(classes = {TerminalDaoImpl.class})
class TerminalDaoImplTest {

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(TERMINAL).execute();
        dslContext.deleteFrom(PROVIDER).execute();
    }


    @Test
    void save() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Terminal terminal = TestObjectFactory.testTerminal();
        terminal.setProviderId(savedProvider.getId());

        terminalDao.save(terminal);

        assertEquals(1, dslContext.fetchCount(TERMINAL));
    }

    @Test
    void saveSameTerminalTwice() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Terminal terminal = TestObjectFactory.testTerminal();
        terminal.setProviderId(savedProvider.getId());

        terminalDao.save(terminal);

        terminal.setDescription("New description");

        terminalDao.save(terminal);

        assertEquals(1, dslContext.fetchCount(TERMINAL));
    }
}