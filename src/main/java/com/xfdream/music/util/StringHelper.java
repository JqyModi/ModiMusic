/**
 * Copyright (c) www.longdw.com
 */
package com.xfdream.music.util;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.DecimalFormat;

public class StringHelper {
	public static enum CharType {
		DELIMITER, // 闈炲瓧姣嶆埅姝㈠瓧绗︼紝渚嬪锛岋紟锛夛紙銆€绛夌瓑銆€锛?鍖呭惈U0000-U0080锛?
		NUM, // 2瀛楄妭鏁板瓧锛戯紥锛擄紨
		LETTER, // gb2312涓殑锛屼緥濡?锛★饥锛ｏ紝2瀛楄妭瀛楃鍚屾椂鍖呭惈 1瀛楄妭鑳借〃绀虹殑 basic latin and latin-1
		OTHER, // 鍏朵粬瀛楃
		CHINESE;// 涓枃瀛?
	}

	/**
	 * 鍒ゆ柇杈撳叆char绫诲瀷鍙橀噺鐨勫瓧绗︾被鍨?
	 * 
	 * @param c
	 *            char绫诲瀷鍙橀噺
	 * @return CharType 瀛楃绫诲瀷
	 */
	public static CharType checkType(char c) {
		CharType ct = null;

		// 涓枃锛岀紪鐮佸尯闂?x4e00-0x9fbb
		if ((c >= 0x4e00) && (c <= 0x9fbb)) {
			ct = CharType.CHINESE;
		}

		// Halfwidth and Fullwidth Forms锛?缂栫爜鍖洪棿0xff00-0xffef
		else if ((c >= 0xff00) && (c <= 0xffef)) { // 2瀛楄妭鑻辨枃瀛?
			if (((c >= 0xff21) && (c <= 0xff3a))
					|| ((c >= 0xff41) && (c <= 0xff5a))) {
				ct = CharType.LETTER;
			}

			// 2瀛楄妭鏁板瓧
			else if ((c >= 0xff10) && (c <= 0xff19)) {
				ct = CharType.NUM;
			}

			// 鍏朵粬瀛楃锛屽彲浠ヨ涓烘槸鏍囩偣绗﹀彿
			else
				ct = CharType.DELIMITER;
		}

		// basic latin锛岀紪鐮佸尯闂?0000-007f
		else if ((c >= 0x0021) && (c <= 0x007e)) { // 1瀛楄妭鏁板瓧
			if ((c >= 0x0030) && (c <= 0x0039)) {
				ct = CharType.NUM;
			} // 1瀛楄妭瀛楃
			else if (((c >= 0x0041) && (c <= 0x005a))
					|| ((c >= 0x0061) && (c <= 0x007a))) {
				ct = CharType.LETTER;
			}
			// 鍏朵粬瀛楃锛屽彲浠ヨ涓烘槸鏍囩偣绗﹀彿
			else
				ct = CharType.DELIMITER;
		}

		// latin-1锛岀紪鐮佸尯闂?080-00ff
		else if ((c >= 0x00a1) && (c <= 0x00ff)) {
			if ((c >= 0x00c0) && (c <= 0x00ff)) {
				ct = CharType.LETTER;
			} else
				ct = CharType.DELIMITER;
		} else
			ct = CharType.OTHER;

		return ct;
	}

	/**
	 * 鑾峰彇缁欏畾姹夊瓧鐨勬嫾闊崇殑棣栧瓧姣?
	 * 
	 * @param c
	 *            涓€涓眽瀛?
	 * @return 濡傛灉c涓嶆槸姹夊瓧锛岃繑鍥?锛涘惁鍒欒繑鍥炶姹夊瓧鐨勬嫾闊崇殑棣栧瓧姣?
	 */
	public static char getPinyinFirstLetter(char c) {
		String[] pinyin = null;
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 涓嶈緭鍑哄０璋?
		try {
			// 鑾峰彇璇ユ眽瀛楃殑鎷奸煶锛堝彲鑳芥槸澶氶煶瀛楋紝鎵€浠ョ敤鏁扮粍瀛樻斁缁撴灉锛?
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		// 濡傛灉c涓嶆槸姹夊瓧锛宼oHanyuPinyinStringArray浼氳繑鍥瀗ull
		if (pinyin == null) {
			return 0;
		}

		// 鍙彇涓€涓彂闊筹紝濡傛灉鏄闊冲瓧锛屼粎鍙栫涓€涓彂闊?
		return pinyin[0].charAt(0);
	}

	public static String bytesToMB(long bytes) {
		float size = (float) (bytes * 1.0 / 1024 / 1024);
		String result = null;
		if (bytes >= (1024 * 1024)) {
			result = new DecimalFormat("###.00").format(size) + "MB";
		} else {
			result = new DecimalFormat("0.00").format(size) + "MB";
		}
		return result;
	}

	/**
	 * 灏嗗瓧绗︿覆涓殑涓枃杞寲涓烘嫾闊?鍏朵粬瀛楃涓嶅彉
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					if (temp == null || TextUtils.isEmpty(temp[0])) {
						continue;
					}
					output += temp[0].replaceFirst(temp[0].substring(0, 1),
							temp[0].substring(0, 1).toUpperCase());
				} else
					output += Character.toString(input[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}
