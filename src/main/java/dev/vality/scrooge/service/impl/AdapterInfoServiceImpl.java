package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AdapterDao;
import dev.vality.scrooge.dao.OptionDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.service.AdapterInfoService;
import dev.vality.scrooge.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdapterInfoServiceImpl implements AdapterInfoService {

    private final AdapterDao adapterDao;
    private final OptionDao optionDao;
    private final EncryptionService encryptionService;

    @Override
    public AdapterInfo get(Integer providerId, Integer termRef) {
        Adapter adapter = adapterDao.getByProviderId(providerId);
        String url = adapter.getUrl();
        List<Option> options = optionDao.getAllByAdapterAndTerminal(adapter.getId(), termRef);
        Map<String, String> optionsMap = options.stream()
                .collect(Collectors.toMap(Option::getKey, option -> encryptionService.decrypt(option.getValue())));
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setUrl(url);
        adapterInfo.setOptions(optionsMap);
        adapterInfo.setProviderId(providerId);
        adapterInfo.setTermRef(termRef);
        return adapterInfo;
    }
}
