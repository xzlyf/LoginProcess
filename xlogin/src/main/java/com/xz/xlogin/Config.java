package com.xz.xlogin;

import androidx.annotation.NonNull;

public class Config {
	protected boolean isLog;
	protected String logTag;
	protected String appId;
	protected String appSecret;
	protected String publicKey;
	protected String version;
	protected String server;

	public Config() {
	}

	/**
	 * 是否开启日志
	 *
	 * @param tag 日志的tag 如果为null则关闭日志
	 */
	public Config log(String tag) {
		this.isLog = tag != null;
		this.logTag = tag;
		return this;
	}

	/**
	 * 设置appid
	 */
	public Config appId(@NonNull String appId) {
		this.appId = appId;
		return this;
	}

	/**
	 * 密钥
	 */
	public Config appSecret(@NonNull String appSecret) {
		this.appSecret = appSecret;
		return this;
	}

	/**
	 * 服务器版本
	 *
	 * @param version
	 * @return
	 */
	public Config serverVersion(@NonNull String version) {
		this.version = version;
		return this;
	}

	/**
	 * 设置服务器
	 */
	public Config server(@NonNull String server) {
		this.server = server;
		return this;
	}

	/**
	 * 设置公钥
	 */
	public Config publicKey(@NonNull String publicKey) {
		this.publicKey = publicKey;
		return this;
	}

	/**
	 * 最后一步
	 */
	public Config build() {
		return this;
	}
}

