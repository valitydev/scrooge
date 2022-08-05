package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Balance;

import java.time.LocalDateTime;

public interface BalanceDao {

    void save(Balance balance);

    LocalDateTime getUpdateTimeByAccount(Long accountId);

}
