package dev.vality.scrooge.service.converter;

import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdapterInfoToOptionConverter {

    private final EncryptionService encryptionService;

    public List<Option> convert(AdapterInfo source, Long adapterId) {
        return source.getOptions().entrySet().stream()
                .map(optionEntry -> {
                    Option option = new Option();
                    option.setKey(optionEntry.getKey());
                    option.setValue(encryptionService.encrypt(optionEntry.getValue()));
                    option.setAdapterId(adapterId);
                    option.setTerminalRef(source.getTermRef());
                    return option;
                })
                .collect(Collectors.toList());
    }
}
