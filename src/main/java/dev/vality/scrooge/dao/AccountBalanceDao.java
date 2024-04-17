package dev.vality.scrooge.dao;

import dev.vality.scrooge.account.AccountBalance;

import java.util.List;

public interface AccountBalanceDao {
    List<AccountBalance> getAllAccountBalances();
}
