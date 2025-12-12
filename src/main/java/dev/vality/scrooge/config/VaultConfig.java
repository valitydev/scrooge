package dev.vality.scrooge.config;

import dev.vality.secret.VaultSecretService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.vault.config.EnvironmentVaultConfiguration;

@Configuration
@Import(EnvironmentVaultConfiguration.class)
public class VaultConfig {

    @Bean
    public VaultSecretService vaultSecretService(EnvironmentVaultConfiguration environmentVaultConfiguration) {
        return new VaultSecretService(environmentVaultConfiguration.vaultTemplate());
    }

}
