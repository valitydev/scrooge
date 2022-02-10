package dev.vality.scrooge.listener;

import dev.vality.fistful.base.EventRange;
import dev.vality.fistful.withdrawal.ManagementSrv;
import dev.vality.fistful.withdrawal.WithdrawalState;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.testcontainers.annotations.KafkaSpringBootTest;
import dev.vality.testcontainers.annotations.kafka.KafkaTestcontainer;
import dev.vality.testcontainers.annotations.kafka.config.KafkaProducer;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@KafkaTestcontainer(
        properties = "kafka.topic.withdrawal.listener.enabled=true",
        topicsKeys = "kafka.topic.withdrawal.id")
@KafkaSpringBootTest
class WithdrawalListenerTest {

    @Value("${kafka.topic.withdrawal.id}")
    private String withdrawalTopicName;
    @Autowired
    private KafkaProducer<TBase<?, ?>> testThriftKafkaProducer;

    @MockBean
    private ManagementSrv.Iface fistfulClient;

    @Test
    void listenWithdrawal() throws TException {
        SinkEvent sinkEvent = TestObjectFactory.testSinkEvent();
        when(fistfulClient.get(anyString(), any(EventRange.class))).thenReturn(new WithdrawalState());

        testThriftKafkaProducer.send(withdrawalTopicName, sinkEvent);

        verify(fistfulClient, timeout(5000).times(1)).get(anyString(), any(EventRange.class));

    }
}