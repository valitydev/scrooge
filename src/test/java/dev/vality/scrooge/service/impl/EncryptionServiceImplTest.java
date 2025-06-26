package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.testcontainers.annotations.postgresql.PostgresqlTestcontainerSingleton;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@PostgresqlTestcontainerSingleton
class EncryptionServiceImplTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void encryptAndDecryptTest() {
        String expectedPayload = TestObjectFactory.randomString();
        String encryptPayload = encryptionService.encrypt(expectedPayload);

        assertNotNull(encryptPayload);

        String actualPayload = encryptionService.decrypt(encryptPayload);

        assertEquals(expectedPayload, actualPayload);
    }
}