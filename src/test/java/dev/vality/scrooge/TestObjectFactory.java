package dev.vality.scrooge;

import dev.vality.fistful.withdrawal.Change;
import dev.vality.fistful.withdrawal.StatusChange;
import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.fistful.withdrawal.status.Status;
import dev.vality.fistful.withdrawal.status.Succeeded;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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

    public static SinkEvent testSinkEvent() {
        MachineEvent machineEvent = new MachineEvent();
        machineEvent.setEventId(randomLong());
        machineEvent.setSourceId(randomString());
        machineEvent.setData(Value.bin(new ThriftSerializer<>().serialize("", testSucceededStatusChange())));
        SinkEvent sinkEvent = new SinkEvent();
        sinkEvent.setEvent(machineEvent);
        return sinkEvent;
    }

    public static TimestampedChange testSucceededStatusChange() {
        return new TimestampedChange()
                .setOccuredAt("2016-03-22T06:12:27Z")
                .setChange(Change.status_changed(
                        new StatusChange().setStatus(
                                Status.succeeded(new Succeeded()))));
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static Long randomLong() {
        return ThreadLocalRandom.current().nextLong(100);
    }

}
