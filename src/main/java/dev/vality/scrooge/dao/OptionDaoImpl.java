package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static dev.vality.scrooge.dao.domain.tables.Option.OPTION;

@Component
public class OptionDaoImpl extends AbstractDao implements OptionDao {

    public OptionDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveAll(List<Option> options) {
        List<Query> queries = options.stream()
                .map(option -> getDslContext().newRecord(OPTION, option))
                .map(optionRecord -> getDslContext()
                        .insertInto(OPTION)
                        .set(optionRecord)
                        .onConflict(OPTION.ID)
                        .doUpdate()
                        .set(getDslContext().newRecord(OPTION, optionRecord)))
                .collect(Collectors.toList());
        batchExecute(queries);
    }
}
