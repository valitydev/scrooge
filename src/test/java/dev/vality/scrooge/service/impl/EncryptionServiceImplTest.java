package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.TestObjectFactory;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.scrooge.service.SecretService;
import dev.vality.testcontainers.annotations.postgresql.PostgresqlTestcontainerSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static dev.vality.scrooge.service.SecretService.ENCRYPTION_IV;
import static dev.vality.scrooge.service.SecretService.ENCRYPTION_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
@PostgresqlTestcontainerSingleton(excludeTruncateTables = "schema_version")
class EncryptionServiceImplTest {

    @Autowired
    private EncryptionService encryptionService;

    @MockitoBean
    private SecretService secretService;


    @BeforeEach
    void setUp() {
        when(secretService.getSecret(ENCRYPTION_KEY)).thenReturn("eSpBf5Dyfq3Vp9zpugIWvA==");
        when(secretService.getSecret(ENCRYPTION_IV)).thenReturn("FpeHXmAMdBu6HFeVocDbtQ==");
    }

    @Test
    void encryptAndDecryptTest() {
        String expectedPayload = TestObjectFactory.randomString();
        String encryptPayload = encryptionService.encrypt(expectedPayload);

        assertNotNull(encryptPayload);

        String actualPayload = encryptionService.decrypt(encryptPayload);

        assertEquals(expectedPayload, actualPayload);
    }
}