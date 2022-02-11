package dev.vality.scrooge.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class BalanceInfo {

    private long amount;
    private String currency;
    private long accountId; // TODO maybe change
    private Instant timestamp;
}

