package com.xz.loginprocess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xz.xlogin.Config;
import com.xz.xlogin.XLogin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

	private TextView tvToken;
	private TextView tvUser;
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvToken = findViewById(R.id.tv_token);
		tvUser = findViewById(R.id.tv_user);

		/*
		 * 初始化
		 * ！！！记得要初始化！！！
		 */
		//XLogin.init(new Config()
		//		.log("xzlyf")
		//		.appId("4MakRN8juW8c6Hii4lTl0rt84JDH22c9")
		//		.appSecret("OoLZs7vHPEPsr9YtZtnLY7My2W2RiwTk")
		//		.serverVersion("1.0")
		//		.server("http://192.168.1.66:8080")
		//		.publicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt5OvgZD9ZuncevvfIMeJU0AiDK01bnMoL9BUhHrf5netVR_ZZ3xNhSDZFlA-wfHiiMMZnUqxsQPBKs8J-zwff-yaYTdg3pvHgsfSePH-_ZIo_bH3meO-_s6l2F0Qqp4pXP7P7lGAI73rYfEAfKiwK79c042PMQdOLYGGnMsB6RwIDAQAB"));
		XLogin.init(new Config()
				.log("xzlyf")
				.appId("4MakRN8juW8c6Hii4lTl0rt84JDH22c9")
				.appSecret("OoLZs7vHPEPsr9YtZtnLY7My2W2RiwTk")
				.serverVersion("1.0")
				.server("http://106.55.150.181/xlogin/")
				.publicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt5OvgZD9ZuncevvfIMeJU0AiDK01bnMoL9BUhHrf5netVR_ZZ3xNhSDZFlA-wfHiiMMZnUqxsQPBKs8J-zwff-yaYTdg3pvHgsfSePH-_ZIo_bH3meO-_s6l2F0Qqp4pXP7P7lGAI73rYfEAfKiwK79c042PMQdOLYGGnMsB6RwIDAQAB"));

		/*
		 *调用登录程序
		 */
		XLogin.login(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == XLogin.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String token = data.getStringExtra(XLogin.EXTRA_TOKEN);
				String user = data.getStringExtra(XLogin.EXTRA_USER);
				String type = data.getStringExtra(XLogin.EXTRA_TYPE);
				tvToken.setText("token:" + token);
				tvUser.setText("user:" + user);
				isLogin = true;

				//verifyUser(user, token, type);

			} else if (resultCode == RESULT_CANCELED) {
				tvToken.setText("登录被终止");
			}
		}
	}

	public void onLogout(View view) {
		if (isLogin) {
			XLogin.logout(MainActivity.this);
			isLogin = false;
			tvToken.setText("token:");
			tvUser.setText("user:");
		} else {
			Toast.makeText(MainActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
			XLogin.login(MainActivity.this);
		}
	}


	/**
	 * 后端验证登录token 防止被篡改
	 *
	 * @param user
	 * @param token
	 * @param type
	 */
	private void verifyUser(String user, String token, String type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.66:8080/app/verifyUser?token=" + token + "&cert=" + user + "&type=" + type + "&appId=4MakRN8juW8c6Hii4lTl0rt84JDH22c9");
					System.out.println("url=" + url.toURI().toString());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(15 * 1000);
					conn.setConnectTimeout(15 * 1000);
					conn.setRequestProperty("appId", "4MakRN8juW8c6Hii4lTl0rt84JDH22c9");
					int code = conn.getResponseCode();
					System.out.println("请求码:" + code);
					if (code == 200) {
						//得到响应流
						InputStream inputStream = conn.getInputStream();
						BufferedReader bufferedReader = new BufferedReader(new
								InputStreamReader(inputStream, StandardCharsets.UTF_8));
						String line = "";
						StringBuilder stringBuilder = new StringBuilder();
						String response = "";
						//每次读取一行，若非空则添加至 stringBuilder
						while ((line = bufferedReader.readLine()) != null) {
							stringBuilder.append(line);
						}
						//读取所有的数据后，赋值给 response
						String res = stringBuilder.toString().trim();
						System.out.println(res);
					}


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
