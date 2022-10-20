package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AdapterDao;
import dev.vality.scrooge.dao.OptionDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AccountInfo;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.service.AdapterInfoService;
import dev.vality.scrooge.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdapterInfoServiceImpl implements AdapterInfoService {

    private final AdapterDao adapterDao;
    private final OptionDao optionDao;
    private final EncryptionService encryptionService;

    @Override
    public AdapterInfo getByAccount(Account account) {
        Adapter adapter = adapterDao.getByProviderId(account.getProviderId());
        AccountInfo accountInfo = AccountInfo.builder()
                .providerId(account.getProviderId())
                .accountId(account.getId())
                .termRef(account.getTerminalRef())
                .adapterId(adapter.getId())
                .build();
        List<Option> options = optionDao.getAllByAccount(accountInfo);
        Map<String, String> optionsMap = options.stream()
                .collect(Collectors.toMap(Option::getKey, option -> encryptionService.decrypt(option.getValue())));
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setUrl(adapter.getUrl());
        adapterInfo.setOptions(optionsMap);
        adapterInfo.setProviderId(account.getProviderId());
        adapterInfo.setTermRef(account.getTerminalRef());
        return adapterInfo;
    }
}
