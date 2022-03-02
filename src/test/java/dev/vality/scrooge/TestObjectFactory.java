package dev.vality.scrooge;

import dev.vality.account_balance.AccountReference;
import dev.vality.account_balance.Balance;
import dev.vality.account_balance.BalanceResponse;
import dev.vality.damsel.domain.ProxyDefinition;
import dev.vality.damsel.payment_processing.ProviderDetails;
import dev.vality.damsel.payment_processing.ProviderTerminal;
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
import dev.vality.scrooge.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    public static WithdrawalTransaction testWithdrawalTransactionFromState(WithdrawalState withdrawalState) {
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setWithdrawalId(withdrawalState.getId());
        transaction.setProviderId(withdrawalState.getRoute().getProviderId());
        transaction.setTerminalId(withdrawalState.getRoute().getTerminalId());
        transaction.setDomainVersionId(withdrawalState.getDomainRevision());
        return transaction;
    }

    public static WithdrawalTransaction testWithdrawalTransaction() {
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setWithdrawalId(randomString());
        transaction.setProviderId(randomInt());
        transaction.setTerminalId(randomInt());
        transaction.setDomainVersionId(randomLong());
        return transaction;
    }

    public static RouteInfo testRouteInfo() {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setAdapterInfo(testAdapterInfo());
        routeInfo.setProviderInfo(testProviderInfo());
        routeInfo.setTerminalInfo(testTerminalInfo());
        return routeInfo;
    }

    public static AdapterInfo testAdapterInfo() {
        AdapterInfo adapterInfo = new AdapterInfo();
        adapterInfo.setUrl("http://adapter:8022/v1");
        adapterInfo.setOptions(Map.of(randomString(), randomString()));
        return adapterInfo;
    }

    public static ProviderInfo testProviderInfo() {
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setName(randomString());
        providerInfo.setDescription(randomString());
        return providerInfo;
    }

    public static TerminalInfo testTerminalInfo() {
        TerminalInfo terminalInfo = new TerminalInfo();
        terminalInfo.setName(randomString());
        terminalInfo.setDescription(randomString());
        return terminalInfo;
    }

    public static BalanceInfo testBalanceInfo() {
        BalanceInfo balanceInfo = new BalanceInfo();
        balanceInfo.setTimestamp(LocalDateTime.now());
        balanceInfo.setCurrency("RUB");
        balanceInfo.setAmount(randomLong());
        balanceInfo.setAccountId(randomString());
        return balanceInfo;
    }

    public static BalanceResponse testBalanceResponse() {
        BalanceResponse balanceResponse = new BalanceResponse();
        Balance balance = new Balance();
        balance.setAmount(randomLong());
        balance.setCurrencyCode(randomString());
        balanceResponse.setBalance(balance);
        AccountReference accountReference = new AccountReference();
        accountReference.setId(randomInt());
        balanceResponse.setAccountReference(accountReference);
        return balanceResponse;
    }

    public static ProviderTerminal testProviderTerminal() {
        return new ProviderTerminal()
                .setName(randomString())
                .setDescription(randomString())
                .setProvider(new ProviderDetails()
                        .setName(randomString())
                        .setDescription(randomString())
                )
                .setProxy(new ProxyDefinition()
                        .setUrl("http://prt-mngmt:8022/v1")
                        .setOptions(Map.of(randomString(), randomString())));
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
