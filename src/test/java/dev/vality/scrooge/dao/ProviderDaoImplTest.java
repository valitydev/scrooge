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

        providerDao.save(provider);

        assertEquals(1, dslContext.fetchCount(PROVIDER));
    }
}