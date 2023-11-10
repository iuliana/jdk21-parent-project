package org.mytoys.one;

import org.junit.jupiter.api.Test;
import org.mytoys.one.kem.Receiver;
import org.mytoys.one.kem.Sender;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

// JEP 452. API for key encapsulation mechanisms (KEMs), an encryption technique for securing symmetric keys using public key cryptography.
public class KeyEncapsulationTest {

 @Test
 void testKEM(){

  var algorithm = "DHKEM";

  var receiver = new Receiver(algorithm);
  //1. generates public and private key
  receiver.generateKeys();

  var sender = new Sender(algorithm);
  // 2.  receiver sends its public key to sender, that uses it to generate a secret value
  sender.generateSecretKey(receiver.getReceiverPublicKey());
  // 3. sender sends the secret value to receiver  as an encapsulation
  sender.sendSecretKeyToReceiver(receiver);
  // 4. receiver uses its private key to decapsulate the encapsulation and obtain the secret value

  //5. obviously the secret values must be equal, because all further communication will be done using this secret value to encrypt it/decrypt it
  assertTrue(Arrays.equals(sender.getSenderSecretKey().getEncoded(),
          receiver.getReceiverSecretKey().getEncoded()));

  /*var message = "Hàlo, a caraid!";
  sender.sendMessage(receiver, message);

  assertEquals(message, receiver.getMessage());*/
 }

}
