package dev.vality.scrooge.service;

import dev.vality.machinegun.eventsink.MachineEvent;

import java.util.List;

public interface EventService {

    void handle(List<MachineEvent> events);

}
