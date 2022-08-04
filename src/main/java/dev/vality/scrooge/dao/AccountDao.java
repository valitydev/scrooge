package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Account;

import java.util.List;

public interface AccountDao {

    Account save(Account account);

    List<Account> getAll();

}
