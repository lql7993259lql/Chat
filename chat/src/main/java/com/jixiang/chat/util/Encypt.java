package com.jixiang.chat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 加密解密
 * @author Wangda
 *
 */
public class Encypt {
	public static String encerpt(String $string, String $operation, String $key, int $expiry) throws Exception {
		String $str = $operation == "DECODE" ? $string : new String($string.getBytes("UTF-8"), "UTF-8");
		int $ckey_length = 10;
		$key = md5($key != null ? $key : "DH-Framework");
		String $keya = md5(substr($key, 0, 16));
		String $keyb = md5(substr($key, 16, 16));
		String $keyc = $ckey_length > 0
				? ($operation.equals("DECODE") ? substr($str, 0, $ckey_length) : substr(md5(1), -$ckey_length)) : "";

		String $cryptkey = $keya + md5($keya + $keyc);

		if ($operation.equals("ENCODE")) {
			$str = sprintf("%010d", $expiry > 0 ? $expiry + time() : 0) + substr(md5($str + $keyb), 0, 16) + $str;
		}
		byte[] stra;
		if ($operation.equals("DECODE")) {
			stra = base64_decode(substr($str, $ckey_length));
		} else {
			stra = $str.getBytes("UTF-8");
		}

		byte[] $box = new byte[256];
		for (int i = 0; i < 256; i++) {
			$box[i] = (byte) i;
		}

		byte[] cryptkey = $cryptkey.getBytes();
		int key_length = cryptkey.length;
		byte[] $rndkey = new byte[256];
		for (int $i = 0; $i <= 255; $i++) {
			$rndkey[$i] = cryptkey[$i % key_length];
		}

		int $j = 0;
		for (int $i = 0; $i < 256; $i++) {
			int boxi = $box[$i];
			int rndkeyi = $rndkey[$i];
			if (boxi < 0) {
				boxi = $box[$i] + 256;
			}
			if (rndkeyi < 0) {
				rndkeyi = $rndkey[$i] + 256;
			}
			$j = ($j + boxi + rndkeyi) % 256;
			byte $tmp = $box[$i];
			$box[$i] = $box[$j];
			$box[$j] = $tmp;
		}

		$j = 0;
		int $a = 0;
		byte[] by = new byte[stra.length];
		for (int $i = 0; $i < stra.length; $i++) {
			$a = ($a + 1) % 256;

			int boxa = $box[$a];
			if (boxa < 0) {
				boxa = boxa + 256;
			}
			$j = ($j + boxa) % 256;
			int boxj = $box[$j];

			if (boxj < 0) {
				boxj = boxj + 256;
			}
			byte $tmp = $box[$a];
			$box[$a] = $box[$j];
			$box[$j] = $tmp;
			by[$i] = (byte) (stra[$i] ^ $box[(boxa + boxj) % 256]);
		}

		if ($operation.equals("DECODE")) {
			String $result = new String(by,"UTF-8").substring(0, new String(by,"UTF-8").length());
			if ((Integer.parseInt(substr($result.toString(), 0, 10)) == 0
					|| Long.parseLong(substr($result.toString(), 0, 10)) - time() > 0)
					&& substr($result.toString(), 10, 16)
							.equals(substr(md5(substr($result.toString(), 26) + $keyb), 0, 16))) {
				return new String(substr($result.toString(), 26).getBytes());
			} else {
				return "";
			}
		} else {
			return $keyc + base64_encode(by).replaceAll("=", "");
		}
	}

	protected static String md5(String input) throws UnsupportedEncodingException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return byte2hex(md.digest(input.getBytes("UTF-8")));
	}

	protected static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString();
	}

	protected static String substr(String input, int begin, int length) {
		return input.substring(begin, begin + length);
	}

	protected static long microtime() {
		return System.currentTimeMillis();
	}

	protected static long time() {
		return System.currentTimeMillis() / 1000;
	}

	protected static String md5(long input) throws UnsupportedEncodingException {
		return md5(String.valueOf(input));
	}

	protected static String substr(String input, int begin) {
		if (begin > 0) {
			return input.substring(begin);
		} else {
			return input.substring(input.length() + begin);
		}
	}

	protected static byte[] base64_decode(String input) {
		try {
			return Base64.decode(input.toCharArray());
		} catch (Exception e) {
			System.out.println("ssss" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	protected static String sprintf(String format, long input) {
		String temp = "0000000000" + input;
		return temp.substring(temp.length() - 10);
	}

	protected static String base64_encode(byte[] a) {
		try {
			return new String(Base64.encode(a));
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
