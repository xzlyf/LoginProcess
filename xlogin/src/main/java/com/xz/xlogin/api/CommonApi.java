package com.xz.xlogin.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.network.NetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/17
 */
public class CommonApi {

	private static final String TAG = UserApi.class.getName();
	private static CommonApi instance;
	private NetUtil netUtil;
	private OkHttpClient client;

	private String sessionId = null;

	private CommonApi() {
		netUtil = NetUtil.getInstance();
		client = netUtil.getOkHttpClient();
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
	public void verifyCode(String code, NetUtil.ResultCallback<ApiResult> callback) {
		Request request;
		if (sessionId == null) {
			request = new Request.Builder()
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_CODE + "?code=" + code)
					.build();
		} else {
			request = new Request.Builder()
					.header("Cookie", sessionId)
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_CODE + "?code=" + code)
					.build();
		}

		netUtil.custom_request(request, callback);

	}

	/**
	 * 获取验证码图片
	 */
	public void verifyCodeImg(DataCallback<Bitmap> callback) {
		Request request;
		Logger.w("会话id:" + sessionId);
		if (sessionId == null) {
			request = new Request.Builder()
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_IMG)
					.build();
		} else {
			request = new Request.Builder()
					.header("Cookie", sessionId)
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_IMG)
					.build();
		}

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
				callback.error(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.code() != 200) {
					callback.error(new Exception("http状态码错误"));
				}

				//获取这次请求的会话id，保证验证时候是同一个会话
				if (sessionId == null) {
					Headers headers = response.headers();
					sessionId = netUtil.getSessionId(headers);
				}
				Bitmap bmp = null;
				InputStream is = response.body().byteStream();
				bmp = BitmapFactory.decodeStream(is);
				is.close();
				callback.data(bmp);


			}
		});
	}


	private Bitmap getNetImage(String imageUrl) {
		Bitmap bmp = null;
		try {
			URL myurl = new URL(imageUrl);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);//设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);//不缓存
			conn.connect();
			if (conn.getResponseCode() != 200) {
				return null;
			}
			InputStream is = conn.getInputStream();//获得图片的数据流
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;

	}


	public interface DataCallback<T> {
		void data(T t);

		void error(Exception e);
	}
}
