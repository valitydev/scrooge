package dev.vality.scrooge.dao;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.PostgresqlJooqTest;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqTest
@ContextConfiguration(classes = {ProviderDaoImpl.class})
class ProviderDaoImplTest {

    @Autowired
    private ProviderDao providerDao;

    @Autowired
    private DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(PROVIDER).execute();
    }


    @Test
    void save() {
        Provider provider = TestObjectFactory.testProvider();

        Provider savedProvider = providerDao.save(provider);

        assertNotNull(savedProvider.getId());
        assertEquals(1, dslContext.fetchCount(PROVIDER));
    }

    @Test
    void saveSameProviderTwice() {
        Provider provider = TestObjectFactory.testProvider();

        Provider savedProvider = providerDao.save(provider);

        savedProvider.setDescription("New description");

        Provider newSavedProvider = providerDao.save(savedProvider);

        assertEquals(savedProvider.getId(), newSavedProvider.getId());
        assertEquals(1, dslContext.fetchCount(PROVIDER));
    }
}