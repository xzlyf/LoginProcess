package com.xz.xlogin.api;

import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.network.NetUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/17
 */
public class CommonApi {

	private static final String TAG = UserApi.class.getName();
	private static CommonApi instance;
	private NetUtil netUtil;

	private CommonApi() {
		netUtil = NetUtil.getInstance();
	}

	public static CommonApi getInstance() {
		if (instance == null) {
			synchronized (CommonApi.class) {
				if (instance == null) {
					instance = new CommonApi();
				}
			}
		}
		return instance;
	}


	/**
	 * 校验验证码
	 */
	public void verifyCode(String code, NetUtil.ResultCallback callback) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		netUtil.get(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_CODE, params, callback);
	}

	/**
	 * 获取验证码图片
	 */
	public void verifyCodeImg() {
	}

}
