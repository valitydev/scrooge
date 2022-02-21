package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AdapterInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdapterInfoToOptionConverter {

    public List<Option> convert(AdapterInfo source, Long adapterId) {
        return source.getOptions().entrySet().stream()
                .map(optionEntry -> {
                    Option option = new Option();
                    option.setKey(optionEntry.getKey());
                    option.setValue(optionEntry.getValue());
                    option.setAdapterId(adapterId);
                    return option;
                })
                .collect(Collectors.toList());
    }
}
