package com.xz.xlogin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.utils.appUtils.ViewUtil;
import com.xz.xlogin.R;
import com.xz.xlogin.XLogin;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.network.StatusEnum;
import com.xz.xlogin.util.RSAUtil;
import com.xz.xlogin.util.TipsDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

public class LoginActivity extends BaseActivity {

	private String userRuleUrl;
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
			if (!safeDom()) {
				return;
			}
			//登录
			login(etUser.getText().toString().trim(), etPwd.getText().toString().trim(), TYPE_PHONE);
		} else if (id == R.id.tv_register) {
			//跳转至注册页面
			startActivity(new Intent(mContext, RegisterActivity.class));
			overridePendingTransition(R.anim.activity_show, R.anim.activity_hide);
		}
	}

	/**
	 * 登陆前检查
	 */
	private boolean safeDom() {
		if (etUser.getText().toString().length() < 4) {
			sToast("用户账号不规范，请重试！");
			return false;
		}
		if (etPwd.getText().toString().length() < 4) {
			sToast("密码长度过短，请重试");
			return false;
		}
		return true;
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
						case 600:
							if (!isLoaded) {
								initView();
							}
							TipsDialogUtil.commonDialogV2(mContext, StatusEnum.getValue(code));
							break;
						case 102:
						case 601:
						case 690:
						case 691:
						case 692:
						case 693:
							if (!isLoaded) {
								initView();
							}
							TipsDialogUtil.commonDialog(mContext, StatusEnum.getValue(code));
							break;
						default:
							if (!isLoaded) {
								initView();
							}
							TipsDialogUtil.systemErrorDialog(mContext);
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
	protected void onDestroy() {
		if (Macroelement.token == null) {
			setResult(RESULT_CANCELED);
		}
		super.onDestroy();
	}
}