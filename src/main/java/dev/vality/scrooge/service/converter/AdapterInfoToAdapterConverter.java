package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.domain.AdapterInfo;
import org.springframework.stereotype.Component;

@Component
public class AdapterInfoToAdapterConverter {

    public Adapter convert(AdapterInfo source, Integer providerId) {
        Adapter adapter = new Adapter();
        adapter.setUrl(source.getUrl());
        adapter.setProviderId(providerId);
        return adapter;
    }
}
