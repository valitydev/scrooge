package dev.vality.scrooge.dao;

import dev.vality.mapper.RecordRowMapper;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.records.AdapterRecord;
import org.jooq.Query;
import org.jooq.SelectConditionStep;
import org.jooq.SelectWhereStep;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

import static dev.vality.scrooge.dao.domain.tables.Adapter.ADAPTER;

@Component
public class AdapterDaoImpl extends AbstractDao implements AdapterDao {

    private final RowMapper<Adapter> listRecordRowMapper;

    public AdapterDaoImpl(DataSource dataSource) {
        super(dataSource);
        listRecordRowMapper = new RecordRowMapper<>(ADAPTER, Adapter.class);
    }

    @Override
    public Adapter save(Adapter adapter) {
        Query query = getDslContext().insertInto(ADAPTER)
                .set(getDslContext().newRecord(ADAPTER, adapter))
                .onConflict(ADAPTER.URL, ADAPTER.PROVIDER_ID)
                .doUpdate()
                .set(getDslContext().newRecord(ADAPTER, adapter))
                .returning(ADAPTER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        adapter.setId(keyHolder.getKey().longValue());
        return adapter;
    }

    @Override
    public List<Adapter> getAll() {
        SelectWhereStep<AdapterRecord> where = getDslContext()
                .selectFrom(ADAPTER);
        return fetch(where, listRecordRowMapper);
    }

    @Override
    public Adapter getByProviderId(Integer providerId) {
        SelectConditionStep<AdapterRecord> where = getDslContext()
                .selectFrom(ADAPTER)
                .where(ADAPTER.PROVIDER_ID.eq(providerId));
        return fetchOne(where, listRecordRowMapper);
    }


}
