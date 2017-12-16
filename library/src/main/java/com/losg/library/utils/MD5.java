package com.losg.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static boolean checkMD5(String md5, File updateFile) throws IOException {
		if (md5 == null || md5 == "" || updateFile == null) {
			return false;
		}

		String calculatedDigest = calculateMD5(updateFile);

		if (calculatedDigest == null) {
			return false;
		}
		return calculatedDigest.equalsIgnoreCase(md5);
	}

	public static String calculateMD5(File updateFile) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(updateFile);
		} catch (FileNotFoundException e) {
			return null;
		}
		byte[] buffer = new byte[8192];
		int read = 0;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			// Fill to 32 chars
			output = String.format("%32s", output).replace(' ', '0');
			return output;
		} catch (IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to closeQuietly input stream for MD5 calculation", e);
			}
		}
	}

	public static final String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
