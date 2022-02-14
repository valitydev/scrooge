package dev.vality.scrooge.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BalanceInfo {

    private long amount;
    private String currency;
    private long accountId; // TODO maybe change
    private LocalDateTime timestamp;
}

