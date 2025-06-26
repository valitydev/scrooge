package dev.vality.scrooge.listener;

import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.service.EventService;
import dev.vality.testcontainers.annotations.KafkaConfig;
import dev.vality.testcontainers.annotations.kafka.KafkaTestcontainerSingleton;
import dev.vality.testcontainers.annotations.kafka.config.KafkaProducer;
import dev.vality.testcontainers.annotations.postgresql.PostgresqlTestcontainerSingleton;
import org.apache.thrift.TBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@KafkaConfig
@KafkaTestcontainerSingleton(
        properties = {
                "kafka.topic.withdrawal.listener.enabled=true",
                "spring.kafka.consumer.auto-offset-reset=earliest"},
        topicsKeys = {
                "kafka.topic.withdrawal.id"})
@PostgresqlTestcontainerSingleton
class WithdrawalListenerTest {

    @Value("${kafka.topic.withdrawal.id}")
    private String withdrawalTopicName;

    @Autowired
    private KafkaProducer<TBase<?, ?>> testThriftKafkaProducer;

    @MockitoBean
    private EventService eventService;

    @Test
    void listenWithdrawal() {
        SinkEvent sinkEvent = TestObjectFactory.testSinkEvent();
        doNothing().when(eventService).handle(List.of(sinkEvent.getEvent()));

        testThriftKafkaProducer.send(withdrawalTopicName, sinkEvent);

        verify(eventService, timeout(5000).times(1)).handle(List.of(sinkEvent.getEvent()));

    }
}