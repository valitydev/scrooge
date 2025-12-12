package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.service.SecretService;
import dev.vality.secret.SecretRef;
import dev.vality.secret.VaultSecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {

    private static final String PATH = "data";

    private final VaultSecretService vaultSecretService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String getSecret(String secretName) {
        return vaultSecretService.getSecret(applicationName, new SecretRef(PATH, secretName)).getValue();
    }


}
