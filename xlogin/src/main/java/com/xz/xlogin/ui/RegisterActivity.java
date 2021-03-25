package com.xz.xlogin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xz.xlogin.R;
import com.xz.xlogin.api.CommonApi;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.network.StatusEnum;
import com.xz.xlogin.ui.fragment.RegisterByAccountFragment;
import com.xz.xlogin.ui.fragment.RegisterByPwdFragment;
import com.xz.xlogin.util.ColorUtil;
import com.xz.xlogin.util.TipsDialogUtil;
import com.xz.xlogin.widget.TipsDialog;
import com.xz.xlogin.widget.VerificationDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import okhttp3.Request;

public class RegisterActivity extends BaseActivity {
	private UserApi userApi;
	private CommonApi commonApi;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	private Fragment mContent;
	private RegisterByAccountFragment accountFragment;
	private RegisterByPwdFragment pwdFragment;
	private String userRuleUrl;
	private CheckBox boxProtocol;
	private TextView tvProtocol;
	private TextView btnSubmit;
	private String userAccount = "";
	private String userPwd = "";
	private int page = 1;//1账号输入fragment 2密码输入fragment

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
		commonApi = CommonApi.getInstance();
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
				if (page == 1) {
					verifyCode();
				} else if (page == 2) {
					submit();
				}
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


	/**
	 * 注册与使用协议
	 */
	private void getProtocol() {
		tvProtocol.setText(Html.fromHtml("《<font color=\"" + ColorUtil.int2Hex(getColor(R.color.colorAccent)) + "\">注册与使用协议</font>》"));
		userRuleUrl = userApi.getUserRules();

	}

	private void initFragment() {
		accountFragment = new RegisterByAccountFragment();
		pwdFragment = new RegisterByPwdFragment();
		manager = getSupportFragmentManager();      //初始化管理者

		transaction = manager.beginTransaction();
		//设置默认默认fragment
		transaction.add(R.id.main_fragment, accountFragment).commit();
		mContent = accountFragment;


		//获取验证码按钮点击时间
		accountFragment.setTimeButtonClickListener(new RegisterByAccountFragment.OnViewClickListener() {
			@Override
			public void onClick(View v, int type) {
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
		showVerificationDialog();


	}

	/**
	 * 显示验证码对话框
	 */
	private void showVerificationDialog() {
		VerificationDialog dialog = new VerificationDialog(mContext);
		dialog.setVerifyStatusCallbackListener(new VerificationDialog.VerifyStatusCallback() {
			@Override
			public void onStartVerify() {

			}

			@Override
			public void onRefreshImg() {

			}

			@Override
			public void onVerifyOverMuch() {
				sToast("刷新频繁，请稍后重试");
			}

			@Override
			public void onVerifySuccess() {
				//验证成功后才开始倒计时
				accountFragment.startClock();
				sendVerifyCode(accountFragment.getAccount(), accountFragment.getType());
			}

			@Override
			public void onVerifyError(String msg) {
				sToast(msg);
			}
		});
		if (isFinishing()) {
			return;
		}
		dialog.create();
		dialog.show();
	}

	/**
	 * 请求服务端发送验证码
	 *
	 * @param type 验证码类型 1手机 2邮箱
	 */
	private void sendVerifyCode(String email, int type) {
		if (type == 1) {
			sToast("手机注册暂时未开放");
		} else if (type == 2) {
			commonApi.getEmailCode(email, new NetUtil.ResultCallback<ApiResult>() {
				@Override
				public void onError(Request request, Exception e) {
					sDialog("异常了", "当前网络异常请稍后重试");
				}

				@Override
				public void onResponse(ApiResult response) {
					switch (response.getCode()) {
						case 1:
							sToast("发送成功，请检查邮箱");
							break;
						case 682:
						case 402:
							accountFragment.endClock();
						default:
							sDialog("提示", StatusEnum.getValue(response.getCode()));
							break;
					}
				}
			});
		} else {
			sToast("注册途径不存在");
		}


	}

	private void verifyCode() {
		int type = accountFragment.getType();
		if (type == 1) {
			sToast("手机注册功能暂未开通");
		} else if (type == 2) {
			submitEmailCode(accountFragment.getAccount(), accountFragment.getCode());
		}
	}

	/**
	 * 验证邮箱验证码
	 */
	private void submitEmailCode(String email, String code) {
		showLoading("请稍后");
		commonApi.verifyEmailCode(email, code, new NetUtil.ResultCallback<ApiResult>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				sDialog("异常了", "当前网络异常请稍后重试");
			}

			@Override
			public void onResponse(ApiResult response) {
				disLoading();
				switch (response.getCode()) {
					case 1:
						//验证成功，让用户设置密码
						page = 2;
						userAccount = email;
						btnSubmit.setText("注册");
						showFragment(pwdFragment);
						break;
					default:
						sDialog("提示", response.getStatus());
						break;
				}
			}
		});
	}

	/**
	 * 提交注册
	 */
	private void submit() {

		int type = accountFragment.getType();
		if (type == 1) {
			register("phone");
		} else if (type == 2) {
			register("email");
		}

	}

	/**
	 * 注册请求
	 *
	 * @param type phone 手机注册 email邮箱诸恶
	 */
	private void register(String type) {
		userPwd = pwdFragment.getPwd();
		if (userPwd.equals("")) {
			return;
		}
		showLoading("正在处理...");
		userApi.register(userAccount, userPwd, "email", new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				TipsDialogUtil.badNetDialog(mContext);
			}

			@Override
			public void onResponse(String response) {
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
											accountFragment.cleanAll();
											Intent intent = new Intent();
											intent.putExtra("userAccount", userAccount);
											setResult(0x4321, intent);
											finish();
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
		overridePendingTransition(R.anim.activity_show2, R.anim.activity_hide2);
	}
}
