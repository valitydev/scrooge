package dev.vality.scrooge.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfo {

    private Integer termRef;
    private Integer providerId;
    private Long adapterId;
    private Long accountId;
}
