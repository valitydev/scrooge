package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;

public interface AccountSurveyService {

    BalanceInfo getBalance(RouteInfo routeInfo);
}
