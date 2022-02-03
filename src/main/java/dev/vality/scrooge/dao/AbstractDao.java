package dev.vality.scrooge.dao;

import dev.vality.dao.impl.AbstractGenericDao;

import javax.sql.DataSource;

public class AbstractDao extends AbstractGenericDao {

    public AbstractDao(DataSource dataSource) {
        super(dataSource);
    }
}
