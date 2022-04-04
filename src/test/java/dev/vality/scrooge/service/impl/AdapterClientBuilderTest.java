package dev.vality.scrooge.service.impl;

import dev.vality.account_balance.AccountServiceSrv;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AdapterClientBuilder.class})
@TestPropertySource(properties = {
        "adapter-client.networkTimeout=5000"})
class AdapterClientBuilderTest {

    @Autowired
    private AdapterClientBuilder adapterClientBuilder;

    @Test
    void build() {
        String stringUrl = "http://adapter-paybox:8022/v1";
        AccountServiceSrv.Iface adapterClient = adapterClientBuilder.build(stringUrl);

        assertNotNull(adapterClient);
    }
}