package dev.vality.scrooge.listener;


import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.eventsink.SinkEvent;
import dev.vality.scrooge.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalListener {

    private final EventService withdrawalService;

    @KafkaListener(
            autoStartup = "${kafka.topic.withdrawal.listener.enabled}",
            topics = "${kafka.topic.withdrawal.id}",
            containerFactory = "withdrawalListenerContainerFactory")
    public void listen(
            List<SinkEvent> batch,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment ack) throws InterruptedException {
        log.info("WithdrawalListener listen withdrawals batch: partition={}, offset={}, batch.size()={}",
                partition, offset, batch.size());
        List<MachineEvent> machineEvents = batch.stream().map(SinkEvent::getEvent).collect(toList());
        withdrawalService.handle(machineEvents);
        ack.acknowledge();
        log.info("WithdrawalListener success committed batch withdrawals: partition={}, offset={}", partition, offset);
        // TODO exception handling
    }
}

