package dev.vality.scrooge.service;

import dev.vality.scrooge.domain.AdapterInfo;

public interface AdapterInfoService {

    AdapterInfo get(Integer providerId, Integer termRef);
}
