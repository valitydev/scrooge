package dev.vality.scrooge.listener;

import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.config.ExcludeDataSourceConfiguration;
import dev.vality.scrooge.service.EventService;
import dev.vality.testcontainers.annotations.KafkaSpringBootTest;
import dev.vality.testcontainers.annotations.kafka.KafkaTestcontainer;
import dev.vality.testcontainers.annotations.kafka.config.KafkaProducer;
import org.apache.thrift.TBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.mockito.Mockito.*;

@KafkaTestcontainer(
        properties = "kafka.topic.withdrawal.listener.enabled=true",
        topicsKeys = "kafka.topic.withdrawal.id")
@KafkaSpringBootTest
@Import(ExcludeDataSourceConfiguration.class)
class WithdrawalListenerTest {

    @Value("${kafka.topic.withdrawal.id}")
    private String withdrawalTopicName;

    @Autowired
    private KafkaProducer<TBase<?, ?>> testThriftKafkaProducer;

    @MockBean
    private EventService eventService;

    @Test
    void listenWithdrawal() {
        SinkEvent sinkEvent = TestObjectFactory.testSinkEvent();
        doNothing().when(eventService).handle(List.of(sinkEvent.getEvent()));

        testThriftKafkaProducer.send(withdrawalTopicName, sinkEvent);

        verify(eventService, timeout(5000).times(1)).handle(List.of(sinkEvent.getEvent()));

    }
}