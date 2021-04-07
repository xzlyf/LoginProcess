package com.xz.xlogin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xz.xlogin.R;
import com.xz.xlogin.XLogin;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.network.StatusEnum;
import com.xz.xlogin.util.RSAUtil;
import com.xz.xlogin.util.RegexUtil;
import com.xz.xlogin.util.TipsDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

public class LoginActivity extends BaseActivity {

	private UserApi userApi;
	private TextView btnSubmit;
	private EditText etUser;
	private EditText etPwd;
	private TextView tvRegister;
	private TextView tvForget;

	private static final String TYPE_PHONE = "phone";//手机登录
	private static final String TYPE_NO = "account";//账号登录
	private static final String TYPE_TOKEN = "token";//token登录
	private static final String TYPE_QQ = "qq";//qq登录
	private static final String TYPE_EMAIL = "email";//邮箱登录
	private boolean isLoaded = false;


	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_login;
	}

	@Override
	public void initData() {
		setTitle("");
		setActionBarColor(Color.TRANSPARENT);
		setActionBarTitleColor(getColor(R.color.colorPrimary));
		setActionBarBackColor(getColor(R.color.colorPrimary));
		changeStatusBarTextColor();
		userApi = UserApi.getInstance();

		if (!autoLogin()) {
			//如果自动登录失败才初始化fragment等页面
			initView();
		}
	}

	/**
	 * 执行自动登录
	 *
	 * @return false 自动登录失败 true 自动登录成功
	 */
	private boolean autoLogin() {
		String rsaToken = XLogin.read(mContext, XLogin.TAG_TOKEN);
		if (rsaToken != null) {
			String rsaUser = XLogin.read(mContext, XLogin.TAG_USER);
			if (rsaUser != null) {
				String user = null;
				String token = null;
				try {
					user = RSAUtil.publicDecrypt(rsaUser, RSAUtil.getPublicKey(Macroelement.localPublicKey));
					token = RSAUtil.publicDecrypt(rsaToken, RSAUtil.getPublicKey(Macroelement.localPublicKey));
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				login(user, token, TYPE_TOKEN);
				return true;
			}
		}
		return false;
	}

	private void initView() {
		isLoaded = true;
		btnSubmit = findViewById(R.id.btn_submit);
		etPwd = findViewById(R.id.et_pwd);
		etUser = findViewById(R.id.et_user);
		tvRegister = findViewById(R.id.tv_register);
		tvForget = findViewById(R.id.tv_forget);
		btnSubmit.setOnClickListener(onViewClickListener);
		tvRegister.setOnClickListener(onViewClickListener);
		tvForget.setOnClickListener(onViewClickListener);

	}


	private View.OnClickListener onViewClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onViewClick(v);
		}
	};

	private void onViewClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_submit) {
			String account = etUser.getText().toString().trim();
			String pwd = etPwd.getText().toString().trim();
			if (account.equals("") || pwd.equals("")) {
				sToast("请先输入账号密码");
				return;
			}
			if (pwd.length() < 6) {
				sToast("密码太短了...");
				return;
			}
			String type = determineAccountType(account);
			if (type.equals("")) {
				return;
			}
			//登录
			login(account, pwd, type);
		} else if (id == R.id.tv_register) {
			//跳转至注册页面
			startActivityForResult(new Intent(mContext, RegisterActivity.class), 0x1234);
			overridePendingTransition(R.anim.activity_show, R.anim.activity_hide);
		} else if (id == R.id.tv_forget) {
			//充值密码页面
			startActivity(new Intent(mContext, ResetActivity.class));
			overridePendingTransition(R.anim.activity_show, R.anim.activity_hide);

		}
	}

	/**
	 * 判断账号类型是手机还是邮箱什么的
	 *
	 * @return 返回登录类型字符串
	 */
	private String determineAccountType(String account) {
		//判断有没有包含@来区分手机号和邮箱号
		if (account.indexOf('@') == -1) {
			boolean isOk = RegexUtil.doRegex(account, RegexUtil.REGEX_MOBILE);
			if (isOk) {
				return TYPE_PHONE;
			} else {
				sToast("手机号格式不正确");
				return "";
			}
		} else {
			boolean isOk = RegexUtil.doRegex(account, RegexUtil.REGEX_EMAIL);
			if (isOk) {
				return TYPE_EMAIL;
			} else {
				sToast("邮箱格式不正确");
				return "";
			}
		}
	}


	/**
	 * 手机号密码登录
	 */
	private void login(String user, String password, String type) {
		Macroelement.user = user;
		if (user.equals("") || password.equals("")) {
			return;
		}
		showLoading("正在登录...");
		userApi.login(type, user, password, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				TipsDialogUtil.badNetDialog(mContext);
				if (!isLoaded) {
					initView();
				}
			}

			@Override
			public void onResponse(String response) {
				disLoading();
				try {
					JSONObject obj = new JSONObject(response);
					int code = obj.optInt("code", -1);
					switch (code) {
						case 1:
							Macroelement.token = obj.optString("data");
							XLogin.save(mContext, XLogin.TAG_TOKEN, Macroelement.token);
							XLogin.save(mContext, XLogin.TAG_USER, Macroelement.user);
							Intent intent = new Intent();
							intent.putExtra(XLogin.EXTRA_TOKEN, Macroelement.token);
							setResult(RESULT_OK, intent);
							finish();
							break;
						default:
							if (!isLoaded) {
								initView();
							}
							TipsDialogUtil.commonDialogV2(mContext, StatusEnum.getValue(code));
							break;
					}


				} catch (JSONException e) {
					e.printStackTrace();
					TipsDialogUtil.serverErrorDialog(mContext);
				}

			}
		});

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x1234 && resultCode == 0x4321) {
			if (data != null) {
				etUser.setText(data.getStringExtra("userAccount"));
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (Macroelement.token == null) {
			setResult(RESULT_CANCELED);
		}
		super.onDestroy();
	}
}