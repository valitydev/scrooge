package dev.vality.scrooge.service.serde;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import dev.vality.sink.common.serialization.BinaryDeserializer;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalChangeMachineEventParser extends MachineEventParser<TimestampedChange> {

    public WithdrawalChangeMachineEventParser(BinaryDeserializer<TimestampedChange> deserializer) {
        super(deserializer);
    }
}