package dev.vality.scrooge.domain;

import lombok.Data;

@Data
public class WithdrawalTransaction {

    private String withdrawalId;
    private int providerId = -1;
    private int terminalId = -1;
    private long domainVersionId = -1;
}
