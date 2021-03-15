package com.xz.xlogin;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.orhanobut.logger.Logger;
import com.xz.utils.fileUtils.StorageUtil;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.fragment.LoginFragment;
import com.xz.xlogin.fragment.RegisterFragment;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.network.StatusEnum;
import com.xz.xlogin.util.ColorUtil;
import com.xz.xlogin.util.IOUtil;
import com.xz.xlogin.util.RSAUtil;
import com.xz.xlogin.util.TipsDialogUtil;
import com.xz.xlogin.widget.TipsDialog;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import okhttp3.Request;

public class LoginActivity extends BaseActivity {

	private FragmentTransaction transaction;
	private FragmentManager manager;
	private Fragment mContent;
	private LoginFragment loginFragment;
	private RegisterFragment registerFragment;
	private String userRuleUrl;


	private UserApi userApi;
	//0 登录  1注册
	private int type = 0;
	private TextView tvLogin;
	private TextView tvRegister;
	private FrameLayout mainFragment;
	private CheckBox boxProtocol;
	private TextView tvProtocol;
	private TextView btnSubmit;


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
		tvLogin = findViewById(R.id.tv_login);
		tvRegister = findViewById(R.id.tv_register);
		mainFragment = findViewById(R.id.main_fragment);
		boxProtocol = findViewById(R.id.box_protocol);
		tvProtocol = findViewById(R.id.tv_protocol);
		btnSubmit = findViewById(R.id.btn_submit);
		tvProtocol.setOnClickListener(onViewClickListener);
		tvLogin.setOnClickListener(onViewClickListener);
		tvRegister.setOnClickListener(onViewClickListener);
		btnSubmit.setOnClickListener(onViewClickListener);

		getProtocol();
		initFragment();
	}

	private void initFragment() {
		loginFragment = new LoginFragment();
		registerFragment = new RegisterFragment();
		//BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager());
		//adapter.addFragment(loginFragment, "登录");

		//FragmentManager fragmentManager = getSupportFragmentManager();
		//FragmentTransaction transaction = fragmentManager.beginTransaction();
		//transaction.add(R.id.main_fragment, loginFragment);
		//transaction.add(R.id.main_fragment, registerFragment);
		//transaction.show(loginFragment);
		//transaction.hide(registerFragment);
		//transaction.commitAllowingStateLoss();
		manager = getSupportFragmentManager();      //初始化管理者

		transaction = manager.beginTransaction();
		//设置默认默认fragment
		transaction.add(R.id.main_fragment, loginFragment).commit();
		mContent = loginFragment;

	}


	/**
	 * 不重新加载fragment的切换fragment
	 */
	private void showFragment(Fragment fragment) {
		if (mContent != fragment) {
			transaction = manager.beginTransaction();
			if (!fragment.isAdded()) {    // 先判断是否被add过
				transaction.hide(mContent).add(R.id.main_fragment, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mContent).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
			}
			mContent = fragment;
		}
	}

	/**
	 * 注册与使用协议
	 */
	private void getProtocol() {
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">注册与使用协议</font>》"));
		userRuleUrl = userApi.getUserRules();

	}


	private View.OnClickListener onViewClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onViewClick(v);
		}
	};

	private void onViewClick(View v) {
		int id = v.getId();
		if (id == R.id.tv_protocol) {
			startActivity(
					new Intent(mContext, WebActivity.class)
							.putExtra("url", userRuleUrl));
		} else if (id == R.id.tv_login) {
			showFragment(loginFragment);
			tvLogin.setBackground(getDrawable(R.drawable.share_text_bottom_line));
			tvRegister.setBackground(getDrawable(R.drawable.share_text_bottom_line_hide));
			type = 0;
			btnSubmit.setText("登录");
		} else if (id == R.id.tv_register) {
			showFragment(registerFragment);
			tvRegister.setBackground(getDrawable(R.drawable.share_text_bottom_line));
			tvLogin.setBackground(getDrawable(R.drawable.share_text_bottom_line_hide));
			type = 1;
			btnSubmit.setText("注册");
		} else if (id == R.id.btn_submit) {
			if (!boxProtocol.isChecked()) {
				sToast("请先阅读并同意");
				return;
			}
			if (type == 0) {
				//登录
				login(loginFragment.getUserNo(), loginFragment.getPwd(), TYPE_PHONE);
			} else if (type == 1) {
				//注册
				phoneRegister();
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


	/**
	 * 手机号注册
	 */
	private void phoneRegister() {
		final String phone = registerFragment.getPhone();
		final String pwd = registerFragment.getPwd();
		if (phone.equals("") || pwd.equals("")) {
			return;
		}
		showLoading("正在加载...", false, null);
		userApi.phoneRegister(phone, pwd, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				TipsDialogUtil.badNetDialog(mContext);

			}

			@Override
			public void onResponse(String response) {
				Logger.w(response);
				disLoading();
				try {
					JSONObject obj = new JSONObject(response);
					int code = obj.getInt("code");
					switch (code) {
						case 1:
							new TipsDialog.Builder(mContext)
									.setType(TipsDialog.STYLE_SUCCESS)
									.setTitle("马上就好啦")
									.setContent("账号注册成功")
									.setSubmitText("马上登录")
									.setOnSubmitListener(new TipsDialog.OnSubmitListener() {
										@Override
										public void onClick(TipsDialog dialog) {
											dialog.dismiss();
											onViewClick(tvLogin);
											loginFragment.setUserNo(phone);
											registerFragment.cleanAll();

										}
									})
									.build()
									.show();
							break;
						case 306:
						case 400:
						case 681:
						case 682:
						case 683:
							new TipsDialog.Builder(mContext)
									.setType(TipsDialog.STYLE_WARN)
									.setTitle("Emmm...")
									.setContent(obj.optString("status"))
									.setSubmitText("让我想想")
									.build()
									.show();
							break;
						default:
							new TipsDialog.Builder(mContext)
									.setType(TipsDialog.STYLE_ERROR)
									.setTitle("Emmm...异常")
									.setContent("系统异常")
									.setSubmitText("那就稍后再试吧")
									.build()
									.show();
							break;
					}

				} catch (JSONException e) {
					e.printStackTrace();
					new TipsDialog.Builder(mContext)
							.setType(TipsDialog.STYLE_ERROR)
							.setTitle("Emmm...奔溃了")
							.setContent("系统异常:" + e.getMessage())
							.setSubmitText("那就稍后再试吧")
							.build()
							.show();
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