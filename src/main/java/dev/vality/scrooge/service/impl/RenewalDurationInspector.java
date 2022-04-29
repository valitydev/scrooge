package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.service.DurationInspector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RenewalDurationInspector implements DurationInspector {

    @Value("${service.renewal.first-threshold}")
    private int firstThreshold;
    @Value("${service.renewal.second-threshold}")
    private int secondThreshold;

    @Override
    public boolean isValid(long duration) {
        if (duration < firstThreshold) {
            return true;
        } else if (duration < secondThreshold) {
            return duration % firstThreshold == 0;
        } else {
            return duration % secondThreshold == 0;
        }
    }
}
