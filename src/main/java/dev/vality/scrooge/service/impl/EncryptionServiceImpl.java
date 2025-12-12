package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.exception.EncryptionException;
import dev.vality.scrooge.service.EncryptionService;
import dev.vality.scrooge.service.SecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import static dev.vality.scrooge.service.SecretService.ENCRYPTION_IV;
import static dev.vality.scrooge.service.SecretService.ENCRYPTION_KEY;

@Service
@RequiredArgsConstructor
public class EncryptionServiceImpl implements EncryptionService {

    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    private final SecretService secretService;

    @Override
    public String encrypt(String payload) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            String key = secretService.getSecret(ENCRYPTION_KEY);
            String iv = secretService.getSecret(ENCRYPTION_IV);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(key), makeIv(iv));
            return new String(Base64.getEncoder().encode(cipher.doFinal(payload.getBytes())));
        } catch (Exception ex) {
            throw new EncryptionException("Error while encrypt", ex);
        }
    }

    private static Key makeKey(String keyBase64) {
        try {
            byte[] key = Base64.getDecoder().decode(keyBase64);
            return new SecretKeySpec(key, ALGORITHM);
        } catch (Exception ex) {
            throw new EncryptionException("Error while create encryption key", ex);
        }
    }

    private AlgorithmParameterSpec makeIv(String ivBase64) {
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        return new IvParameterSpec(iv);
    }

    @Override
    public String decrypt(String encryptedPayload) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            String key = secretService.getSecret(ENCRYPTION_KEY);
            String iv = secretService.getSecret(ENCRYPTION_IV);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(key), makeIv(iv));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedPayload)));
        } catch (Exception ex) {
            throw new EncryptionException("Error while decrypt", ex);
        }
    }
}
