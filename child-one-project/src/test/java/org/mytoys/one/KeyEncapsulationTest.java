package org.mytoys.one;

import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.crypto.DecapsulateException;
import javax.crypto.KEM;
import javax.crypto.SecretKey;
import java.security.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// JEP 8. API for key encapsulation mechanisms (KEMs), an encryption technique for securing symmetric keys using public key cryptography.
//  It differs from the more traditional technique that encrypts a randomly generated symmetric key with a public key. According to the JEP, the KEM mechanism is simpler and also addresses several disadvantages of the traditional approach.
// the KEM API proposal is intended to enable applications to make use of KEM and KEM algorithms such as
//  RSA Key Encapsulation Mechanism (RSA-KEM),
//  Elliptic Curve Integration Encryption Scheme (ECIES), and candidate KEM algorithms for the National Institute of Standards and Technology (NIST) Post-Quantum Cryptography (PQC) standardization process.
// The plan is to enable the use of KEM in higher-level protocols such as Transport Layer Security (TLS).
// KEMs are a crucial tool for defending against quantum attacks.
// The KEM API consists of three functions:
//   -> a key pair generation function, (the old KeyPairGenerator API was kept)
//   -> a key encapsulation function,
//   -> and a key decapsulation function.
public class KeyEncapsulationTest {

 @Disabled
 @Test
 void testKEM() throws NoSuchAlgorithmException, InvalidKeyException, DecapsulateException {
  Security.addProvider(new BouncyCastleProvider());
  KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
  generator.initialize(2048);
  KeyPair pair = generator.generateKeyPair();

  PrivateKey privateKey = pair.getPrivate(); // receiver
  PublicKey publicKey = pair.getPublic(); // sender
  //// -------------------------------------------------------------------


  // sender
  // retrieve publicKey
  KEM kemSender = KEM.getInstance("RSA-KEM", new BouncyCastleProvider());
  assertNotNull(kemSender);
  KEM.Encapsulator encapsulator = kemSender.newEncapsulator(publicKey);
  KEM.Encapsulated encapsulatedPublicKey = encapsulator.encapsulate();
  SecretKey senderKey = encapsulatedPublicKey.key();
  // send it to receiver

  // receiver
  // receive the key (mock stuff here)
  byte[] senderMessage = senderKey.getEncoded();
  KEM kemReceiver = KEM.getInstance("RSA-KEM", new BouncyCastleProvider());
  assertNotNull(kemReceiver);

  KEM.Decapsulator decapsulator = kemReceiver.newDecapsulator(privateKey);
  SecretKey receivedKey = decapsulator.decapsulate(senderMessage);

  assertEquals(receivedKey, senderKey);

 }
}
