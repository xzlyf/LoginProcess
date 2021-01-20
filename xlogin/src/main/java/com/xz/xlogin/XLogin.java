package com.xz.xlogin;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.xz.xlogin.constant.Macroelement;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/20
 */
public class XLogin {

	public static final int REQUEST_CODE = 0x0001212;
	public static final String EXTRA_TOKEN = "token";

	public static void init(XLoginBuilder builder) {
		if (builder.isLog) {
			log(builder.logTag);
		}
		Macroelement.appId = builder.appId;
		Macroelement.appSecret = builder.appSecret;
		Macroelement.version = builder.version;
		Macroelement.publicKey = builder.publicKey;
		Macroelement.SERVER = builder.server;
		Macroelement.BASE_URL_USER = Macroelement.SERVER + Macroelement.alt_user;
		Macroelement.BASE_URL_TODO = Macroelement.SERVER + Macroelement.alt_todolist;
		Macroelement.BASE_URL_INFO = Macroelement.SERVER + Macroelement.alt_appinfo;

	}

	/**
	 * 前往登录页面
	 * 通过onActivityResult来获取回调信息
	 * RESULT_OK 会携带token
	 */
	public static void login(Activity activity) {
		//activity.startActivity(new Intent(activity, LoginActivity.class));
		activity.startActivityForResult(new Intent(activity, LoginActivity.class), REQUEST_CODE);

	}


	private static void log(String tag) {
		Logger.init(tag)    //LOG TAG默认是PRETTYLOGGER
				.methodCount(2)                 // 决定打印多少行（每一行代表一个方法）默认：2
				.methodOffset(0);
	}

	public static class XLoginBuilder {
		private String logTag;
		private boolean isLog;
		private String appId;
		private String appSecret;
		private String publicKey;
		private String version;
		private String server;

		public XLoginBuilder() {
		}

		/**
		 * 是否开启日志
		 *
		 * @param tag 日志的tag 如果为null则关闭日志
		 */
		public XLoginBuilder log(String tag) {
			this.isLog = tag != null;
			this.logTag = tag;
			return this;
		}

		/**
		 * 设置appid
		 */
		public XLoginBuilder appId(@NonNull String appId) {
			this.appId = appId;
			return this;
		}

		/**
		 * 密钥
		 */
		public XLoginBuilder appSecret(@NonNull String appSecret) {
			this.appSecret = appSecret;
			return this;
		}

		/**
		 * 服务器版本
		 *
		 * @param version
		 * @return
		 */
		public XLoginBuilder serverVersion(@NonNull String version) {
			this.version = version;
			return this;
		}

		/**
		 * 设置服务器
		 */
		public XLoginBuilder server(@NonNull String server) {
			this.server = server;
			return this;
		}

		/**
		 * 设置公钥
		 */
		public XLoginBuilder publicKey(@NonNull String publicKey) {
			this.publicKey = publicKey;
			return this;
		}

		/**
		 * 最后一步
		 */
		public XLoginBuilder build() {
			return this;
		}
	}


	/**
	 * 登录回调
	 */
	public interface LoginCallback {
		void onLogin(Intent intent);

		void onCancel(Intent intent);

		void onError(Exception e);
	}

}
