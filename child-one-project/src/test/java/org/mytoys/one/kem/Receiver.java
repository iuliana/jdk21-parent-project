package org.mytoys.one.kem;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Receiver {
    private final String algorithm;
    private Cipher cipher;

    SecretKey receiverSecretKey;

    public Receiver(String algorithm) {
        this.algorithm = algorithm;
    }

    PublicKey receiverPublicKey;
    PrivateKey receiverPrivateKey;

    public void generateKeys(){
        try {
            KeyPairGenerator receiverKPG = KeyPairGenerator.getInstance("X25519");
            KeyPair receiverKP = receiverKPG.generateKeyPair();
            receiverPublicKey = receiverKP.getPublic();
            receiverPrivateKey = receiverKP.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate public & private key!!", e);
        }
    }

    public PublicKey getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void receiveSecretKey(byte[] encapsulation){
        try {
        KEM kemReceiver = KEM.getInstance(algorithm);
        // Creates a KEM decapsulator on the KEM receiver side.
        KEM.Decapsulator receiverDCAP = kemReceiver.newDecapsulator(receiverPrivateKey);
        receiverSecretKey = receiverDCAP.decapsulate(encapsulation);
        } catch (NoSuchAlgorithmException | InvalidKeyException | DecapsulateException e) {
            throw new RuntimeException("Could not decapsulate secret key!!", e);
        }
    }

    public SecretKey getReceiverSecretKey() {
        return receiverSecretKey;
    }

    private  String message;

    public void receiveMessage(byte[] encryptedMessage){
        try {
            cipher = Cipher.getInstance(receiverPrivateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, receiverSecretKey);
            byte[] decriptedMessage = cipher.doFinal(encryptedMessage);
            message =  new String(decriptedMessage, StandardCharsets.UTF_8);
        }  catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                  NoSuchPaddingException e) {
            throw new RuntimeException("Could receive and decrypt message!", e);
        }
    }

    public String getMessage() {
        return message;
    }
}
