package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.domain.RouteInfo;

public interface StateService {

    void update(RouteInfo routeInfo, BalanceInfo balanceInfo);
}
