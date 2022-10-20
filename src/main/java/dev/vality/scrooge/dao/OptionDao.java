package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AccountInfo;

import java.util.List;

public interface OptionDao {

    void saveAll(List<Option> options);

    List<Option> getAllByAccount(AccountInfo accountInfo);
}
