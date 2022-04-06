package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Terminal;
import org.jooq.Query;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static dev.vality.scrooge.dao.domain.tables.Terminal.TERMINAL;

@Component
public class TerminalDaoImpl extends AbstractDao implements TerminalDao {

    public TerminalDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Terminal terminal) {
        Query query = getDslContext().insertInto(TERMINAL)
                .set(getDslContext().newRecord(TERMINAL, terminal))
                .onConflict(TERMINAL.TERMINAL_REF)
                .doUpdate()
                .set(getDslContext().newRecord(TERMINAL, terminal));
        execute(query);
    }
}
