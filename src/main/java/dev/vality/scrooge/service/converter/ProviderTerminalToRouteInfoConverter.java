package dev.vality.scrooge.service.converter;

import dev.vality.damsel.domain.ProxyDefinition;
import dev.vality.damsel.payment_processing.ProviderDetails;
import dev.vality.damsel.payment_processing.ProviderTerminal;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.ProviderInfo;
import dev.vality.scrooge.domain.RouteInfo;
import dev.vality.scrooge.domain.TerminalInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProviderTerminalToRouteInfoConverter implements Converter<ProviderTerminal, RouteInfo> {

    @Override
    public RouteInfo convert(ProviderTerminal source) {
        ProviderDetails provider = source.getProvider();
        ProviderInfo providerInfo = convertProvider(provider);
        TerminalInfo terminalInfo = convertTerminal(source);
        ProxyDefinition adapter = source.getProxy();
        AdapterInfo adapterInfo = convertAdapter(adapter);
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setProviderInfo(providerInfo);
        routeInfo.setTerminalInfo(terminalInfo);
        routeInfo.setAdapterInfo(adapterInfo);
        return routeInfo;
    }

    private ProviderInfo convertProvider(ProviderDetails provider) {
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setDescription(provider.getDescription());
        providerInfo.setName(provider.getName());
        return providerInfo;
    }

    private TerminalInfo convertTerminal(ProviderTerminal source) {
        TerminalInfo terminalInfo = new TerminalInfo();
        terminalInfo.setDescription(source.getDescription());
        terminalInfo.setName(source.getName());
        return terminalInfo;
    }

    private AdapterInfo convertAdapter(ProxyDefinition adapter) {
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setOptions(adapter.getOptions());
        adapterInfo.setUrl(adapter.getUrl());
        return adapterInfo;
    }
}
