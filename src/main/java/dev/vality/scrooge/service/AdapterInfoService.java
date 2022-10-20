package dev.vality.scrooge.service;

import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.domain.AdapterInfo;

public interface AdapterInfoService {

    AdapterInfo getByAccount(Account account);
}
