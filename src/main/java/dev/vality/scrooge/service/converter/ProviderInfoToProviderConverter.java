package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.domain.ProviderInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProviderInfoToProviderConverter implements Converter<ProviderInfo, Provider> {

    @Override
    public Provider convert(ProviderInfo source) {
        Provider provider = new Provider();
        provider.setName(source.getName());
        provider.setDescription(source.getDescription());
        return provider;
    }
}
