package com.xz.xlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.xz.utils.fileUtils.StorageUtil;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.ui.LoginActivity;
import com.xz.xlogin.util.IOUtil;
import com.xz.xlogin.util.RSAUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/20
 */
public class XLogin {

	public static final int REQUEST_CODE = 0x0001212;
	public static final String EXTRA_TOKEN = "token";
	public static final String TAG_TOKEN = "token";
	public static final String TAG_USER = "user";

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
		Macroelement.BASE_URL_INFO = Macroelement.SERVER + Macroelement.alt_appinfo;
		Macroelement.BASE_URL_APP = Macroelement.SERVER + Macroelement.alt_app;
	}


	/**
	 * 外部开放接口
	 * 前往登录页面
	 * 通过onActivityResult来获取回调信息
	 * RESULT_OK 会携带token
	 */
	public static void login(Activity activity) {
		//activity.startActivity(new Intent(activity, LoginActivity.class));
		activity.startActivityForResult(new Intent(activity, LoginActivity.class), REQUEST_CODE);

	}

	/**
	 * 外部开放接口
	 * 注销登录
	 * 注销后会取消自动登录
	 */
	public static void logout(Context context) {
		String rsaToken = read(context, TAG_TOKEN);
		if (rsaToken != null) {
			String rsaUser = read(context, TAG_USER);
			if (rsaUser != null) {
				String user = null;
				String token = null;
				try {
					user = RSAUtil.publicDecrypt(rsaUser, RSAUtil.getPublicKey(Macroelement.localPublicKey));
					token = RSAUtil.publicDecrypt(rsaToken, RSAUtil.getPublicKey(Macroelement.localPublicKey));
					UserApi api = UserApi.getInstance();
					//注销接口
					Logger.w("账号：" + user);
					Logger.w("token：" + token);
					api.logout(user, token);
					//删除自动登录文件
					delete(context, TAG_USER);
					delete(context, TAG_TOKEN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	private static void log(String tag) {
		Logger.init(tag)    //LOG TAG默认是PRETTYLOGGER
				.methodCount(2)                 // 决定打印多少行（每一行代表一个方法）默认：2
				.methodOffset(0);
	}

	protected static boolean delete(Context context, String child) {
		String path = StorageUtil.getDataDir(context);
		File file = new File(path, child);
		if (file.exists()) {
			return file.delete();
		}
		return false;

	}

	public static void save(Context context, String child, String data) {
		String path = StorageUtil.getDataDir(context);
		File file = new File(path, child);
		FileOutputStream fos = null;

		try {
			if (!file.exists()) {
				boolean isCreate = file.createNewFile();
				if (!isCreate) {
					throw new IOException("File create error");
				}
			}
			String rsa = RSAUtil.privateEncrypt(data, RSAUtil.getPrivateKey(Macroelement.localPrivateKey));
			fos = new FileOutputStream(file);
			fos.write(rsa.getBytes());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeAll(fos);
		}

	}

	public static String read(Context context, String child) {
		String path = StorageUtil.getDataDir(context);
		File file = new File(path, child);
		FileInputStream fis = null;
		try {
			if (!file.exists()) {
				return null;
			}
			fis = new FileInputStream(file);
			byte[] buff = new byte[1024];
			int len = fis.read(buff);
			return new String(buff, 0, len);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeAll(fis);
		}
		return null;
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
