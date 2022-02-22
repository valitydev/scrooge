package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;

public interface AccountSurveyService {

    BalanceInfo getBalance(AdapterInfo adapterInfo);
}
