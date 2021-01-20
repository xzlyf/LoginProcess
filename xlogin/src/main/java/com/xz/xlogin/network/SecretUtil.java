package com.xz.xlogin.network;

import com.xz.utils.encodUtils.MD5Util;
import com.xz.xlogin.constant.Macroelement;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/6
 */
public class SecretUtil {

	/**
	 * 1.0 sign加密规则
	 *
	 * @param timestamp 时间戳
	 */
	public static String getSecret(long timestamp) {
		return MD5Util.getMD5(Macroelement.appId + Macroelement.appSecret + timestamp + Macroelement.version);
	}
}
