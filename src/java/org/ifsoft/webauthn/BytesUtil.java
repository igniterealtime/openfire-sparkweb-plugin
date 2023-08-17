package org.ifsoft.webauthn;

import java.util.Base64;

public class BytesUtil {

  public static byte[] stringToBytes(String string) {
	return Base64.getDecoder().decode(string);
  }

  public static String bytesToString(byte[] bytes) {
	return Base64.getEncoder().encodeToString(bytes);
  }

}	