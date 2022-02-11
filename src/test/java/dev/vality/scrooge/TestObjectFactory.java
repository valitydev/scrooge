package dev.vality.scrooge;

import dev.vality.fistful.withdrawal.*;
import dev.vality.fistful.withdrawal.status.Status;
import dev.vality.fistful.withdrawal.status.Succeeded;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.machinegun.msgpack.Value;
import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;
import dev.vality.scrooge.dao.domain.tables.pojos.Option;
import dev.vality.scrooge.dao.domain.tables.pojos.Provider;
import dev.vality.scrooge.domain.WithdrawalTransaction;

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
        MachineEvent machineEvent = testMachineEvent();
        SinkEvent sinkEvent = new SinkEvent();
        sinkEvent.setEvent(machineEvent);
        return sinkEvent;
    }

    public static MachineEvent testMachineEvent() {
        MachineEvent machineEvent = new MachineEvent();
        machineEvent.setSourceNs(randomString());
        machineEvent.setEventId(randomLong());
        machineEvent.setSourceId(randomString());
        machineEvent.setCreatedAt(randomString());
        machineEvent.setData(Value.bin(new ThriftSerializer<>().serialize("", testSucceededStatusChange())));
        return machineEvent;
    }

    public static TimestampedChange testSucceededStatusChange() {
        return new TimestampedChange()
                .setOccuredAt("2016-03-22T06:12:27Z")
                .setChange(Change.status_changed(
                        new StatusChange().setStatus(
                                Status.succeeded(new Succeeded()))));
    }

    public static WithdrawalState testWithdrawalState() {
        Route route = new Route();
        route.setProviderId(randomInt());
        route.setTerminalId(randomInt());
        WithdrawalState withdrawalState = new WithdrawalState();
        withdrawalState.setRoute(route);
        withdrawalState.setId(randomString());
        withdrawalState.setDomainRevision(randomLong());
        return withdrawalState;
    }

    public static WithdrawalTransaction testWithdrawalTransaction(WithdrawalState withdrawalState) {
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setWithdrawalId(withdrawalState.getId());
        transaction.setProviderId(withdrawalState.getRoute().getProviderId());
        transaction.setTerminalId(withdrawalState.getRoute().getTerminalId());
        transaction.setDomainVersionId(withdrawalState.getDomainRevision());
        return transaction;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static Long randomLong() {
        return ThreadLocalRandom.current().nextLong(100);
    }

    public static Integer randomInt() {
        return ThreadLocalRandom.current().nextInt(100);
    }

}
