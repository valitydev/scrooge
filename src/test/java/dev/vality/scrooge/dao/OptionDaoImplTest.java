package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.AdapterRecord;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Option.OPTION;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        int optionCount = 3;
        List<Option> options = TestObjectFactory.testOptions(optionCount, savedAdapter.getId());

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
        int optionCount = 3;
        List<Option> options = TestObjectFactory.testOptions(optionCount, savedAdapter.getId());

        optionDao.saveAll(options);
        List<Option> newOptions = List.of(options.get(0), TestObjectFactory.testOption(savedAdapter.getId()));
        optionDao.saveAll(newOptions);

        assertEquals(optionCount + 1, dslContext.fetchCount(OPTION));
    }
}