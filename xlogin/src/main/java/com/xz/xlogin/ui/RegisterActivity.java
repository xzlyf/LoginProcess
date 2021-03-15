package com.xz.xlogin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.orhanobut.logger.Logger;
import com.xz.xlogin.R;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.ui.fragment.RegisterByPhoneFragment;
import com.xz.xlogin.ui.fragment.RegisterByPwdFragment;
import com.xz.xlogin.util.ColorUtil;
import com.xz.xlogin.util.TipsDialogUtil;
import com.xz.xlogin.widget.TipsDialog;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

public class RegisterActivity extends BaseActivity {
	private int layoutNum = 1;//1手机  2邮箱  3密码·
	private UserApi userApi;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	private Fragment mContent;
	private RegisterByPhoneFragment phoneFragment;
	private RegisterByPwdFragment pwdFragment;
	private String userRuleUrl;
	private CheckBox boxProtocol;
	private TextView tvProtocol;
	private TextView btnSubmit;

	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_register;
	}

	@Override
	public void initData() {
		setTitle("");
		setActionBarColor(Color.TRANSPARENT);
		setActionBarTitleColor(getColor(R.color.colorPrimary));
		setActionBarBackColor(getColor(R.color.colorPrimary));
		changeStatusBarTextColor();
		userApi = UserApi.getInstance();
		initView();
		initFragment();
		btnSubmit.setText("下一步");
	}

	private void initView() {
		boxProtocol = findViewById(R.id.box_protocol);
		tvProtocol = findViewById(R.id.tv_protocol);
		btnSubmit = findViewById(R.id.btn_submit);

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showFragment(pwdFragment);
			}
		});
		tvProtocol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(
						new Intent(mContext, WebActivity.class)
								.putExtra("url", userRuleUrl));
			}
		});

		getProtocol();

	}

	private void initFragment() {
		phoneFragment = new RegisterByPhoneFragment();
		pwdFragment = new RegisterByPwdFragment();
		manager = getSupportFragmentManager();      //初始化管理者

		transaction = manager.beginTransaction();
		//设置默认默认fragment
		transaction.add(R.id.main_fragment, phoneFragment).commit();
		mContent = phoneFragment;


		phoneFragment.setTimeButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getAuthCode();
			}
		});

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
	 * 是否已同意用户条例
	 *
	 * @return
	 */
	private boolean isAllow() {
		return boxProtocol.isChecked();
	}

	/**
	 * 获取验证码
	 */
	private void getAuthCode() {

		if (!isAllow()) {
			sToast("请先阅读并同意用户使用条例");
			return;
		}

		// TODO: 2021/3/15 获取验证码api
		//请求api后才开始计时
		phoneFragment.startClock();
	}


	/**
	 * 注册与使用协议
	 */
	private void getProtocol() {
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">注册与使用协议</font>》"));
		userRuleUrl = userApi.getUserRules();

	}

	/**
	 * 手机号注册
	 */
	private void phoneRegister() {
		final String phone = phoneFragment.getPhone();
		final String pwd = pwdFragment.getPwd();
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
											phoneFragment.cleanAll();

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


	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_show2,R.anim.activity_hide2);
	}
}
