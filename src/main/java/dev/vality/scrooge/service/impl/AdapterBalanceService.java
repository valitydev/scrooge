package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.dao.AccountDao;
import dev.vality.scrooge.dao.BalanceDao;
import dev.vality.scrooge.dao.OptionDao;
import dev.vality.scrooge.dao.domain.tables.pojos.Account;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Balance;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.domain.AdapterInfo;
import dev.vality.scrooge.domain.BalanceInfo;
import dev.vality.scrooge.service.AccountSurveyService;
import dev.vality.scrooge.service.BalanceService;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.scrooge.service.converter.BalanceInfoToAccountConverter;
import dev.vality.scrooge.service.converter.BalanceInfoToBalanceConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterBalanceService implements BalanceService<Adapter> {

    private final BalanceDao balanceDao;
    private final AccountDao accountDao;
    private final OptionDao optionDao;
    private final AccountSurveyService accountSurveyService;
    private final BalanceInfoToAccountConverter accountConverter;
    private final BalanceInfoToBalanceConverter balanceConverter;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public void update(Adapter adapter) {
        AdapterInfo adapterInfo = buildAdapterInfo(adapter);
        BalanceInfo balanceInfo = accountSurveyService.getBalance(adapterInfo);
        if (Objects.nonNull(balanceInfo)) {
            Account account = accountConverter.convert(balanceInfo, adapter.getProviderId());
            Account savedAccount = accountDao.save(account);
            Balance balance = balanceConverter.convert(balanceInfo, savedAccount.getId());
            balanceDao.save(balance);
            log.info("Success update balance for account {}", account.getNumber());
        }
    }

    private AdapterInfo buildAdapterInfo(Adapter adapter) {
        String url = adapter.getUrl();
        List<Option> options = optionDao.getAllByAdapter(adapter.getId());
        Map<String, String> optionsMap = options.stream()
                .collect(Collectors.toMap(Option::getKey, option -> encryptionService.decrypt(option.getValue())));
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setUrl(url);
        adapterInfo.setOptions(optionsMap);
        return adapterInfo;
    }
}
