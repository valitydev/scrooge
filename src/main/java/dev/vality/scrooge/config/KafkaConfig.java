package dev.vality.scrooge.config;

import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.scrooge.listener.serde.SinkEventDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Value("${kafka.topic.withdrawal.listener.max-poll-records}")
    private String withdrawalMaxPollRecords;

    @Value("${kafka.topic.withdrawal.listener.concurrency}")
    private int withdrawalConcurrency;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MachineEvent> withdrawalListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MachineEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        DefaultKafkaConsumerFactory<String, MachineEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerConfigs());
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(withdrawalConcurrency);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        var properties = kafkaProperties.buildConsumerProperties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SinkEventDeserializer.class);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientId());
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, withdrawalMaxPollRecords);
        return properties;
    }


}
