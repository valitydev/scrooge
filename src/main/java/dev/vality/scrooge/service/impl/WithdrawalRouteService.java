package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.WithdrawalTransaction;
import dev.vality.scrooge.service.RouteService;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalRouteService implements RouteService<WithdrawalTransaction> {

    // TODO erlang new methods for terminal info

    @Override
    public RouteInfo get(WithdrawalTransaction transaction) {
        return null;
    }
}
