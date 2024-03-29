package dev.vality.scrooge.dao;

import dev.vality.scrooge.terminal.balance.TerminalBalance;

import java.util.List;

public interface TerminalBalanceDao {
    List<TerminalBalance> getAllTerminalBalances();
}
