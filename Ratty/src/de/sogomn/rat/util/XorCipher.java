package de.sogomn.rat.util;

public final class XorCipher {
	
	private static final byte DEFAULT_KEY = 56;
	
	private XorCipher() {
		//...
	}
	
	public static byte[] crypt(final byte[] data, final byte key) {
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte)(data[i] ^ key);
		}
		
		return data;
	}
	
	public static byte[] crypt(final byte[] data) {
		return crypt(data, DEFAULT_KEY);
	}
	
}
