package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.dao.domain.tables.records.ProviderRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;
import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqTest
@ContextConfiguration(classes = {AdapterDaoImpl.class})
class AdapterDaoImplTest {

    @Autowired
    private AdapterDao adapterDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(ADAPTER).execute();
        dslContext.deleteFrom(PROVIDER).execute();
    }

    @Test
    void save() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());

        Adapter savedAdapter = adapterDao.save(adapter);

        assertNotNull(savedAdapter.getId());
        assertEquals(1, dslContext.fetchCount(ADAPTER));
    }

    @Test
    void saveSameAdapterTwice() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());

        Adapter savedAdapter = adapterDao.save(adapter);


        Adapter newSavedAdapter = adapterDao.save(savedAdapter);

        assertEquals(savedAdapter.getId(), newSavedAdapter.getId());
        assertEquals(1, dslContext.fetchCount(ADAPTER));
    }

    @Test
    void getAll() {
        Provider provider = TestObjectFactory.testProvider();
        dslContext.insertInto(PROVIDER)
                .set(dslContext.newRecord(PROVIDER, provider))
                .execute();
        ProviderRecord savedProvider = dslContext.fetchAny(PROVIDER);
        Adapter adapter = TestObjectFactory.testAdapter(savedProvider.getId());
        Adapter secondAdapter = TestObjectFactory.testAdapter(savedProvider.getId());
        dslContext.insertInto(ADAPTER)
                .set(dslContext.newRecord(ADAPTER, adapter))
                .newRecord()
                .set(dslContext.newRecord(ADAPTER, secondAdapter))
                .execute();

        List<Adapter> savedAdapters = adapterDao.getAll();

        assertEquals(2, savedAdapters.size());
    }
}