package dev.vality.scrooge.dao;

import dev.vality.mapper.RecordRowMapper;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.dao.domain.tables.records.OptionRecord;
import dev.vality.scrooge.domain.AccountInfo;
import org.jooq.Query;
import org.jooq.SelectConditionStep;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static dev.vality.scrooge.dao.domain.tables.Option.OPTION;

@Component
public class OptionDaoImpl extends AbstractDao implements OptionDao {

    private final RowMapper<Option> listRecordRowMapper;

    public OptionDaoImpl(DataSource dataSource) {
        super(dataSource);
        listRecordRowMapper = new RecordRowMapper<>(OPTION, Option.class);
    }

    @Override
    public void saveAll(List<Option> options) {
        List<Query> queries = options.stream()
                .map(option -> getDslContext().newRecord(OPTION, option))
                .map(optionRecord -> getDslContext()
                        .insertInto(OPTION)
                        .set(optionRecord)
                        .onConflict(OPTION.ADAPTER_ID, OPTION.KEY, OPTION.TERMINAL_REF, OPTION.ACCOUNT_ID)
                        .doUpdate()
                        .set(getDslContext().newRecord(OPTION, optionRecord)))
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public List<Option> getAllByAccount(AccountInfo accountInfo) {
        SelectConditionStep<OptionRecord> where = getDslContext()
                .selectFrom(OPTION)
                .where(OPTION.ADAPTER_ID.eq(accountInfo.getAdapterId())
                        .and(OPTION.TERMINAL_REF.eq(accountInfo.getTermRef()))
                        .and(OPTION.ACCOUNT_ID.eq(accountInfo.getAccountId())));
        return fetch(where, listRecordRowMapper);
    }
}
