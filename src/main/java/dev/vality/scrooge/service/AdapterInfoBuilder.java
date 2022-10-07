package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.AdapterInfo;

public interface AdapterInfoBuilder {

    AdapterInfo build(Integer providerId, Integer termRef);
}
