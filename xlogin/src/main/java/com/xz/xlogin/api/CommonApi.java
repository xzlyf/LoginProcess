package com.xz.xlogin.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.network.NetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

	private static String sessionId = null;

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
	 * 获取验证码图片
	 */
	public void verifyCodeImg(DataCallback<Bitmap> callback) {
		Request request;
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
				//if (sessionId == null) {
					Headers headers = response.headers();
					sessionId = netUtil.getSessionId(headers);
				//}
				Bitmap bmp = null;
				InputStream is = response.body().byteStream();
				bmp = BitmapFactory.decodeStream(is);
				is.close();
				callback.data(bmp);


			}
		});
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
	 * 获取网络图片 转为 bitmap
	 */
	public Bitmap getNetImage(String imageUrl) {
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

	/**
	 * 请求服务器发送手机验证码
	 * todo 待完成 请求服务器发送手机验证码
	 */
	public void getPhoneCode() {

	}

	/**
	 * 向服务器请求邮箱验证码发送
	 */
	public void getEmailCode(String email, NetUtil.ResultCallback<ApiResult> callback) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		//填装body
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			builder.add(entry.getKey(), entry.getValue().toString());
		}
		Request request;
		if (sessionId == null) {
			request = new Request.Builder()
					.post(builder.build())
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_EMAIL_CODE)
					.build();
		} else {
			request = new Request.Builder()
					.header("Cookie", sessionId)
					.post(builder.build())
					.url(Macroelement.BASE_URL_APP + Macroelement.GET_EMAIL_CODE)
					.build();
		}
		netUtil.custom_request(request, callback);
	}


	public interface DataCallback<T> {
		void data(T t);

		void error(Exception e);
	}
}
