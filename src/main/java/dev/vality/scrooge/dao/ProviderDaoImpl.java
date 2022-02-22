package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Provider.PROVIDER;

@Component
public class ProviderDaoImpl extends AbstractDao implements ProviderDao {

    public ProviderDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Provider save(Provider provider) {
        Query query = getDslContext().insertInto(PROVIDER)
                .set(getDslContext().newRecord(PROVIDER, provider))
                .onConflict(PROVIDER.ID)
                .doUpdate()
                .set(getDslContext().newRecord(PROVIDER, provider))
                .returning(PROVIDER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        provider.setId(keyHolder.getKey().intValue());
        return provider;
    }
}
