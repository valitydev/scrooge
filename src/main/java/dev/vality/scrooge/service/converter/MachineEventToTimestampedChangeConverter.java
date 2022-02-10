package dev.vality.scrooge.service.converter;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MachineEventToTimestampedChangeConverter {

    private final MachineEventParser<TimestampedChange> parser;

    public TimestampedChange convert(MachineEvent event) {
        if (!event.isSetData()) {
            return null;
        }
        return parser.parse(event);
    }

}
