package dev.vality.scrooge.service;

public interface SecretService {

    String ENCRYPTION_KEY = "encryption_key";
    String ENCRYPTION_IV = "encryption_iv";

    String getSecret(String secretName);
}
