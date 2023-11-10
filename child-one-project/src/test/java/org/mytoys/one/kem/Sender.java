package org.mytoys.one.kem;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Sender {

    private final String algorithm;
    private Cipher cipher;

    SecretKey senderSecretKey;
    KEM.Encapsulated  senderENC;


    public Sender(String algorithm) {
        this.algorithm = algorithm;
    }

    public void generateSecretKey(PublicKey receiverPublicKey){
        KEM kemSender;
        try {
            kemSender = KEM.getInstance(algorithm);
            // Creates a KEM encapsulator on the KEM sender side.
            KEM.Encapsulator e = kemSender.newEncapsulator(receiverPublicKey);

            //Each invocation of the encapsulate method generates a secret key and key encapsulation message that is returned in an KEM.
            senderENC = e.encapsulate();
            senderSecretKey = senderENC.key();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Could not generate secret key!!", e);
        }
    }

    public void sendSecretKeyToReceiver(Receiver receiver){
        receiver.receiveSecretKey(senderENC.encapsulation());
    }

    public SecretKey getSenderSecretKey() {
        return senderSecretKey;
    }

    public void sendMessage(Receiver receiver, String message) {
        try {
            cipher = Cipher.getInstance(senderSecretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, senderSecretKey);
            var encryptedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            receiver.receiveMessage(encryptedMessage);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            throw new RuntimeException("Could encrypt & send message!", e);
        }
    }
}
