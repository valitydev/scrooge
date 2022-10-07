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
        ProxyDefinition adapter = source.getProxy();
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setProviderInfo(convertProviderInfo(provider));
        routeInfo.setTerminalInfo(convertTerminalInfo(source));
        int termRef = source.getRef().getId();
        routeInfo.setAdapterInfo(convertAdapterInfo(adapter, termRef));
        return routeInfo;
    }

    private ProviderInfo convertProviderInfo(ProviderDetails provider) {
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setDescription(provider.getDescription());
        providerInfo.setName(provider.getName());
        providerInfo.setReferenceId(provider.getRef().getId());
        return providerInfo;
    }

    private TerminalInfo convertTerminalInfo(ProviderTerminal source) {
        TerminalInfo terminalInfo = new TerminalInfo();
        terminalInfo.setReferenceId(source.getRef().getId());
        terminalInfo.setDescription(source.getDescription());
        terminalInfo.setName(source.getName());
        return terminalInfo;
    }

    private AdapterInfo convertAdapterInfo(ProxyDefinition adapter, int termRef) {
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setOptions(adapter.getOptions());
        adapterInfo.setUrl(adapter.getUrl());
        adapterInfo.setTermRef(termRef);
        return adapterInfo;
    }
}
