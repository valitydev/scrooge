package dev.vality.scrooge.service;

public interface EncryptionService {

    String encrypt(String payload);

    String decrypt(String encryptedPayload);
}
