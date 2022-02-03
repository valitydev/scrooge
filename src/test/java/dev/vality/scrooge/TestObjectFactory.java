package dev.vality.scrooge;

import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestObjectFactory {


    public static Provider testProvider() {
        Provider provider = new Provider();
        provider.setDescription(randomString());
        provider.setName(randomString());
        return provider;
    }

    private static Option testOption(Long adapterId) {
        Option option = new Option();
        option.setAdapterId(adapterId);
        option.setKey(randomString());
        option.setValue(randomString());
        return option;
    }

    public static List<Option> testOptions(int i, Long adapterId) {
        return IntStream.rangeClosed(1, i)
                .mapToObj(value -> testOption(adapterId))
                .collect(Collectors.toList());
    }

    public static Adapter testAdapter(Integer providerId) {
        Adapter adapter = new Adapter();
        adapter.setUrl(randomString());
        adapter.setProviderId(providerId);
        return adapter;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

}
