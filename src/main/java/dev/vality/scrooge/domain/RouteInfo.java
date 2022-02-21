package dev.vality.scrooge.domain;

import lombok.Data;

@Data
public class RouteInfo {

    private AdapterInfo adapterInfo;
    private ProviderInfo providerInfo;
    private TerminalInfo terminalInfo;

}
