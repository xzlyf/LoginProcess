package com.xz.xlogin.network;

import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.util.MD5Util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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
	public static String getSign(long timestamp) {
		return MD5Util.getMD5(Macroelement.appId + Macroelement.appSecret + timestamp + Macroelement.version);
	}

	/**
	 * 2.0 sign加密
	 * 规则：根据key的ANSI码从小到大排序得到
	 * MD5(AppId+Key=Value+Key=Value...+Key=Value+AppSecret+ServerVersion)
	 * （+号 =号 省略）
	 */
	public static String getSign(Map<String, Object> params) {
		Map<String, Object> newParams = sortMapByKey(params);
		StringBuilder sb = new StringBuilder();
		sb.append(Macroelement.appId);
		for (String key : newParams.keySet()) {
			sb.append(key);
			sb.append(newParams.get(key));
		}
		sb.append(Macroelement.appSecret);
		sb.append(Macroelement.version);
		return MD5Util.getMD5(sb.toString());
	}


	/**
	 * 使用 Map按key进行排序
	 *
	 * @param map
	 * @return
	 */
	private static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		sortMap.putAll(map);
		return sortMap;

	}
}

