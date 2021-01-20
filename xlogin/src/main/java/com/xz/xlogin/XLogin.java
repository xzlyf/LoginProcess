package com.xz.xlogin;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.xz.xlogin.content.Local;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/20
 */
public class XLogin {


	public static void init(XLoginBuilder builder) {
		if (builder.isLog) {
			log(builder.logTag);
		}
		Local.appId = builder.appId;
		Local.appSecret = builder.appSecret;
		Local.version = builder.version;
		Local.publicKey = builder.publicKey;
		Local.BASE_URL_USER = Local.SERVER + Local.alt_user;
		Local.BASE_URL_TODO = Local.SERVER + Local.alt_todolist;
		Local.BASE_URL_INFO = Local.SERVER + Local.alt_appinfo;

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

}
