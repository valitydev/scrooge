package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;

@Component
public class AdapterDaoImpl extends AbstractDao implements AdapterDao {

    public AdapterDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Adapter save(Adapter adapter) {
        Query query = getDslContext().insertInto(ADAPTER)
                .set(getDslContext().newRecord(ADAPTER, adapter))
                .onConflict(ADAPTER.ID)
                .doUpdate()
                .set(getDslContext().newRecord(ADAPTER, adapter))
                .returning(ADAPTER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        adapter.setId(keyHolder.getKey().longValue());
        return adapter;
    }
}
