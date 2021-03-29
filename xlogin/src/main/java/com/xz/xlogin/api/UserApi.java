package com.xz.xlogin.api;

import androidx.annotation.NonNull;

import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.util.MD5Util;
import com.xz.xlogin.util.RSAUtil;
import com.xz.xlogin.util.StringUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/12
 * rxhttp使用教程：https://www.cnblogs.com/liujingxing/p/12106117.html
 */
public class UserApi {
	private static final String TAG = UserApi.class.getName();
	private static UserApi instance;
	private NetUtil netUtil;

	private UserApi() {
		netUtil = NetUtil.getInstance();
	}

	public static UserApi getInstance() {
		if (instance == null) {
			synchronized (UserApi.class) {
				if (instance == null) {
					instance = new UserApi();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取用户条例地址
	 */
	public String getUserRules() {
		return Macroelement.BASE_URL_INFO + Macroelement.GET_USER_RULE + "?appId=" + Macroelement.appId;
	}

	/**
	 * 手机注册
	 */
	public void register(String phone, String pwd, @NonNull String type, NetUtil.ResultCallback<String> callback) {

		Map<String, Object> params = new HashMap<>();
		try {
			params.put("pwd", RSAUtil.publicEncrypt(pwd, RSAUtil.getPublicKey(Macroelement.publicKey)));
			//params.put("password", pwd);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			System.out.println("RSA operation Err");
			callback.onError(null, e);
			return;
		}
		params.put("cert", phone);
		params.put("type", type);
		params.put("t", System.currentTimeMillis());
		params.put("st", MD5Util.getMD5(String.valueOf(Math.random())));
		netUtil.post(Macroelement.BASE_URL_USER + Macroelement.GET_REGISTER, params, callback);
	}

	/**
	 * 登录
	 *
	 * @param type 1-手机登录 2-账号登录 3-token登录
	 */
	public void login(String type, String account, String pwd, NetUtil.ResultCallback<String> callback) {
		long timestamp = System.currentTimeMillis();
		Map<String, Object> params = new HashMap<>();
		params.put("cert", account);
		params.put("type", type);
		params.put("t", timestamp);
		params.put("st", StringUtil.getRandomString(8));
		try {
			//密码规则=明文密码+时间戳
			//params.put("pwd", RSAUtil.publicEncrypt(pwd + timestamp, RSAUtil.getPublicKey(Macroelement.publicKey)));
			params.put("pwd", RSAUtil.publicEncrypt(pwd, RSAUtil.getPublicKey(Macroelement.publicKey)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			System.out.println("RSA operation Err");
			callback.onError(null, e);
			return;
		}

		netUtil.post(timestamp, Macroelement.BASE_URL_USER + Macroelement.GET_LOGIN, params, callback);
	}

	/**
	 * 重置密码
	 */
	public void reset() {
		// TODO: 2021/3/29 完善重置密码接口 
	}

	/**
	 * 注销登录
	 */
	public void logout(String userNo, String token) {
		Map<String, Object> params = new HashMap<>();
		params.put("cert", userNo);
		params.put("token", token);
		netUtil.get(Macroelement.BASE_URL_USER + Macroelement.GET_LOGOUT, params, new NetUtil.ResultCallback<ApiResult<String>>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(ApiResult<String> response) {
				//不管是否正确的token都可以注销
				System.out.println("logout");
			}
		});

	}


}
