package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.config.properties.SecurityProperties;
import dev.vality.scrooge.exception.EncryptionException;
import dev.vality.scrooge.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

@Service
@RequiredArgsConstructor
public class EncryptionServiceImpl implements EncryptionService {

    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    private final SecurityProperties properties;


    @Override
    public String encrypt(String payload) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(properties.getKey()), makeIv(properties.getIv()));
            return new String(Base64.encodeBase64(cipher.doFinal(payload.getBytes())));
        } catch (Exception ex) {
            throw new EncryptionException("Error while encrypt", ex);
        }
    }

    private static Key makeKey(String encryptionKey) {
        try {
            byte[] key = Base64.decodeBase64(encryptionKey);
            return new SecretKeySpec(key, ALGORITHM);
        } catch (Exception ex) {
            throw new EncryptionException("Error while create encryption key", ex);
        }
    }

    private AlgorithmParameterSpec makeIv(String iv) {
        try {
            return new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            throw new EncryptionException("Error while create init vector", ex);
        }
    }

    @Override
    public String decrypt(String encryptedPayload) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, makeKey(properties.getKey()), makeIv(properties.getIv()));
            return new String(cipher.doFinal(Base64.decodeBase64(encryptedPayload)));
        } catch (Exception ex) {
            throw new EncryptionException("Error while decrypt", ex);
        }
    }
}
