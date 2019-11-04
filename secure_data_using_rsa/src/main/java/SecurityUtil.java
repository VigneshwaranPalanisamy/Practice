package main.java;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.cert.Certificate;

public class SecurityUtil {

	private static String keystorePassword = "aspire@123";
	private static String keystoreAlias = "rsatest";		
	private static String publicKey = "";
	private static String privateKey ="";

	static {
		readKeyPairFromKeyStore();
	}

	/**
	 * this method will read key pairs from the given keystore file path 
	 */
	public static void readKeyPairFromKeyStore() {
		try {
			FileInputStream is = new FileInputStream("rsa_test_demo.jks");
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, keystorePassword.toCharArray());
			PrivateKey key = (PrivateKey) keystore.getKey(keystoreAlias, keystorePassword.toCharArray());
			Certificate cert = keystore.getCertificate(keystoreAlias);
			PublicKey publickey = cert.getPublicKey();
			publicKey = Base64.getEncoder().encodeToString(publickey.getEncoded());
			privateKey = Base64.getEncoder().encodeToString(key.getEncoded());
			System.out.println("Static files Loaded");
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String readPrivateKey(String fileLocation) {
		try {
			FileInputStream is = new FileInputStream(fileLocation);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, keystorePassword.toCharArray());
			PrivateKey key = (PrivateKey) keystore.getKey(keystoreAlias, keystorePassword.toCharArray());
			privateKey = Base64.getEncoder().encodeToString(key.getEncoded());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return privateKey;
	}
	
	private static String readPublicKey(String fileLocation) {
		try {
			//FileInputStream is = new FileInputStream("resource/rsa_test_demo.jks");
			FileInputStream is = new FileInputStream(fileLocation);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, keystorePassword.toCharArray());
			Certificate cert = keystore.getCertificate(keystoreAlias);
			PublicKey publickey = cert.getPublicKey();
			publicKey = Base64.getEncoder().encodeToString(publickey.getEncoded());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return publicKey;
	}

	public static PublicKey getPublicKey(String base64PublicKey){
		PublicKey publicKey = null;
		try{
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey){
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static String encrypt(String fileLocation, String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		String publicKey  = readPublicKey(fileLocation);
		if(publicKey != null && !publicKey.isEmpty()) {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
			String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
			return encryptedString;
		} else {
			return "Error Occured while encrypting your data.";
		}
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(String fileLocation, String data) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String privateKey  = readPrivateKey(fileLocation);
		if(publicKey != null && !publicKey.isEmpty()) {
			return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(privateKey));
		} else {
			return "Error Occured while decrypting your data.";
		}
	}

	public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
		try {
			String encryptedString = encrypt("rsa_test_demo.jks","Sample Data");
			System.out.println(encryptedString);
			String decryptedString = decrypt("rsa_test_demo.jks",encryptedString);
			System.out.println(decryptedString);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}

	}
}
