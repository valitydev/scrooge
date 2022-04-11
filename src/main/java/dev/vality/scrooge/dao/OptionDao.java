package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Option;

import java.util.List;

public interface OptionDao {

    void saveAll(List<Option> options);

    List<Option> getAllByAdapter(Long id);
}
