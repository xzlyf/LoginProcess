package com.xz.xlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.ui.LoginActivity;
import com.xz.xlogin.util.IOUtil;
import com.xz.xlogin.util.RSAUtil;
import com.xz.xlogin.util.StorageUtil;

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
	public static final String EXTRA_USER= "user";
	public static final String EXTRA_TYPE = "type";
	public static final String TAG_TOKEN = "token";
	public static final String TAG_USER = "user";
	private static UserApi api;

	public static void init(Config builder) {
		if (builder.isLog) {
			//todo 日志系统开发
		}
		api = UserApi.getInstance();
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
					//注销接口
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


	public static boolean delete(Context context, String child) {
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


}
