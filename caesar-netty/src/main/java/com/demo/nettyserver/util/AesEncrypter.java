/**
 * 
 */
package com.demo.nettyserver.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;

public class AesEncrypter {

	private final static Logger log = Logger.getLogger(AesEncrypter.class);

	private final static int CRYPTO_KEYLEN = 128;

	private Cipher ecipher;

	private Cipher dcipher;
	
	public AesEncrypter(String key) {

		Key k = new SecretKeySpec(StringUtils.getBytesUtf8(key), "AES");

		try {
			ecipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			ecipher.init(Cipher.ENCRYPT_MODE, k);
			dcipher.init(Cipher.DECRYPT_MODE, k);

		} catch (GeneralSecurityException e) {
			log.error(e.getMessage());
			throw new IllegalArgumentException(e);
		}
	}

	public byte[] decrypt(String ciphertext) {

		byte[] d = StringUtils.getBytesUtf8(ciphertext);

		try {
			return dcipher.doFinal(d);
		} catch (IllegalBlockSizeException e) {
			log.warn(e.getMessage());
		} catch (BadPaddingException e) {
			log.warn(e.getMessage());
		}

		return null;
	}

	public String decryptString(String ciphertext) {

		return decryptString(Base64.decodeBase64(ciphertext));
	}

	public String decryptString(byte[] data) {

		try {
			return StringUtils.newStringUtf8(dcipher.doFinal(data));
		} catch (IllegalBlockSizeException e) {
			log.warn(e.getMessage());
		} catch (BadPaddingException e) {
			log.warn(e.getMessage());
		}

		return null;
	}

	public byte[] encrypt(String text) {

		byte[] d = StringUtils.getBytesUtf8(text);

		try {
			return ecipher.doFinal(d);
		} catch (IllegalBlockSizeException e) {
			log.warn(e.getMessage());
		} catch (BadPaddingException e) {
			log.warn(e.getMessage());
		}

		return null;
	}

	public String encryptString(String text) {

		byte[] d = StringUtils.getBytesUtf8(text);

		try {
			return Base64.encodeBase64URLSafeString(ecipher.doFinal(d));
		} catch (IllegalBlockSizeException e) {
			log.warn(e.getMessage());
		} catch (BadPaddingException e) {
			log.warn(e.getMessage());
		}

		return null;
	}

	public static boolean validateKey(String key) {

		if (key == null) {
			return false;
		}

		int keylen = StringUtils.getBytesUtf8(key).length * 8;

		return keylen >= CRYPTO_KEYLEN;
	}
	
	public static void main(String[] args) {
		AesEncrypter aes = new AesEncrypter("6177870978030123");
		System.out.println(aes.decryptString("nz1TV8832aMvbS55GPGUMWsXIi-3P-tbp60NqtrVcQCWFNv2g5iVF-rEIM192E8F"));
	}
}
