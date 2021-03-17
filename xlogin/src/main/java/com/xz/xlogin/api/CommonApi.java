package com.xz.xlogin.api;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/17
 */
public class CommonApi {

	private static final String TAG = UserApi.class.getName();
	private static CommonApi instance;
	private CommonApi api;

	private CommonApi() {
		api = CommonApi.getInstance();
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
	public void verifyCode(String code) {

	}

}
