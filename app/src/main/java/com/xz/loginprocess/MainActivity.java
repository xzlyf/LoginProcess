package com.xz.loginprocess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xz.xlogin.XLogin;

public class MainActivity extends AppCompatActivity {

	private TextView tvToken;
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvToken = findViewById(R.id.tv_token);

		/*
		 * 初始化
		 * ！！！记得要初始化！！！
		 */
		XLogin.init(new XLogin.XLoginBuilder()
				.log("xzlyf")
				.appId("4MakRN8juW8c6Hii4lTl0rt84JDH22c9")
				.appSecret("OoLZs7vHPEPsr9YtZtnLY7My2W2RiwTk")
				.serverVersion("1.0")
				.server("http://192.168.1.66")
				.publicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt5OvgZD9ZuncevvfIMeJU0AiDK01bnMoL9BUhHrf5netVR_ZZ3xNhSDZFlA-wfHiiMMZnUqxsQPBKs8J-zwff-yaYTdg3pvHgsfSePH-_ZIo_bH3meO-_s6l2F0Qqp4pXP7P7lGAI73rYfEAfKiwK79c042PMQdOLYGGnMsB6RwIDAQAB")
				.build());

		/*
		 *调用登录程序
		 */
		//startActivity(new Intent(this, LoginActivity.class));
		XLogin.login(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == XLogin.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				tvToken.setText("token:" + data.getStringExtra(XLogin.EXTRA_TOKEN));
				isLogin = true;
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
		} else {
			Toast.makeText(MainActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
			XLogin.login(MainActivity.this);
		}
	}
}
