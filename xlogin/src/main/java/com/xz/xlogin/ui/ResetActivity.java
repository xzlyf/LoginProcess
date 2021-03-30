package com.xz.xlogin.ui;

import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.api.CommonApi;
import com.xz.xlogin.api.UserApi;
import com.xz.xlogin.base.BaseActivity;
import com.xz.xlogin.entity.ApiResult;
import com.xz.xlogin.network.NetUtil;
import com.xz.xlogin.network.StatusEnum;
import com.xz.xlogin.util.RegexUtil;
import com.xz.xlogin.util.TipsDialogUtil;
import com.xz.xlogin.widget.TimeButton;
import com.xz.xlogin.widget.TipsDialog;
import com.xz.xlogin.widget.VerificationDialog;

import okhttp3.Request;

public class ResetActivity extends BaseActivity {

	private LinearLayout layoutSelect;
	private ImageView selectPhone;
	private ImageView selectEmail;
	private TextView btnSubmit;
	private LinearLayout layoutInput;
	private EditText etAccount;
	private EditText etCode;
	private EditText etPwd;
	private ImageView alterPwd;
	private TimeButton btnTime;

	private UserApi userApi;
	private CommonApi commonApi;
	private int mType = -1; //1手机号 2邮箱号
	private String mAccount;
	private String mPwd;

	@Override
	public boolean homeAsUpEnabled() {
		return true;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.activity_reset;
	}

	@Override
	public void initData() {
		setTitle("");
		setActionBarColor(Color.TRANSPARENT);
		setActionBarTitleColor(getColor(R.color.colorPrimary));
		setActionBarBackColor(getColor(R.color.colorPrimary));
		changeStatusBarTextColor();
		initView();
		commonApi = CommonApi.getInstance();
		userApi = UserApi.getInstance();
	}

	private void initView() {
		layoutSelect = findViewById(R.id.layout_select);
		selectPhone = findViewById(R.id.select_phone);
		selectEmail = findViewById(R.id.select_email);
		btnSubmit = findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(onSubmitClick);
		layoutSelect.setVisibility(View.VISIBLE);
		btnSubmit.setVisibility(View.INVISIBLE);
		btnSubmit.setEnabled(false);
		selectEmail.setOnClickListener(onSelectViewClick);
		selectPhone.setOnClickListener(onSelectViewClick);

		layoutInput = findViewById(R.id.layout_input);
		etAccount = findViewById(R.id.et_account);
		etCode = findViewById(R.id.et_code);
		etPwd = findViewById(R.id.et_pwd);
		alterPwd = findViewById(R.id.alter_pwd);
		btnTime = findViewById(R.id.btn_time);
		btnTime.setOnClickListener(onTimeClick);
		layoutInput.setVisibility(View.GONE);

		alterPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPasswordEye(etPwd);
			}
		});

	}

	/**
	 * 设置密码可见和不可见
	 */
	private void setPasswordEye(EditText editText) {
		if (EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == editText.getInputType()) {
			//如果不可见就设置为可见
			editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
			editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			alterPwd.setImageResource(R.mipmap.ic_invisible);
		} else {
			//如果可见就设置为不可见
			editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			alterPwd.setImageResource(R.mipmap.ic_visible);
		}
		//执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
		editText.setSelection(editText.getText().toString().length());
	}

	private void inputLayout() {
		layoutSelect.setVisibility(View.GONE);
		layoutInput.setVisibility(View.VISIBLE);
		btnSubmit.setEnabled(true);
		btnSubmit.setVisibility(View.VISIBLE);

		if (mType == 1) {
			etAccount.setHint("手机号");
		} else if (mType == 2) {
			etAccount.setHint("邮箱号");
		}
	}

	private View.OnClickListener onSubmitClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			submit();
		}
	};


	private View.OnClickListener onSelectViewClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.select_email) {
				mType = 2;
			} else if (v.getId() == R.id.select_phone) {
				mType = 1;
			}
			inputLayout();
		}
	};
	private View.OnClickListener onTimeClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String account = etAccount.getText().toString().trim();
			//判断空
			if (account.equals("")) {
				if (mType == 1) {
					//手机验证码
					sToast("请先输入手机号");
					return;
				} else if (mType == 2) {
					//邮箱获取验证码
					sToast("请先输入邮箱号");
					return;
				}
			} else {
				if (mType == 1) {
					//手机验证码
					if (!RegexUtil.doRegex(account, RegexUtil.REGEX_MOBILE)) {
						sToast("手机号格式错误");
						return;
					}
				} else if (mType == 2) {
					//邮箱获取验证码
					if (!RegexUtil.doRegex(account, RegexUtil.REGEX_EMAIL)) {
						sToast("邮箱格式错误");
						return;
					}

				}
			}
			mAccount = account;
			showVerificationDialog();
		}
	};

	private VerificationDialog dialog;

	/**
	 * 显示验证码对话框
	 */
	private void showVerificationDialog() {
		if (dialog != null) {
			dialog.show();
			dialog.refreshCode();
			return;
		}
		dialog = new VerificationDialog(mContext);
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
				btnTime.start();
				sendVerifyCode(mAccount, mType);
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
	private void sendVerifyCode(String account, int type) {
		if (type == 1) {
			sToast("手机注册暂时未开放");
		} else if (type == 2) {
			commonApi.getEmailCode(account, "reset", new NetUtil.ResultCallback<ApiResult>() {
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
							btnTime.end();
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

	private void submit() {
		showLoading("正在处理...");
		mPwd = etPwd.getText().toString().trim();
		if (mPwd.equals("")) {
			return;
		}
		if (mPwd.length() < 6) {
			sToast("密码强度若，密码不可少于6位");
			return;
		} else if (mPwd.length() > 16) {
			sToast("密码过长");
			return;
		}
		String code = etCode.getText().toString().trim();
		if (code.equals("")) {
			sToast("请输入验证码");
			return;
		}

		userApi.reset(mAccount, mPwd, mType, code, new NetUtil.ResultCallback<ApiResult>() {
			@Override
			public void onError(Request request, Exception e) {
				disLoading();
				TipsDialogUtil.badNetDialog(mContext);
			}

			@Override
			public void onResponse(ApiResult response) {
				disLoading();
				if (response.getCode() == 1) {
					new TipsDialog.Builder(mContext)
							.setType(TipsDialog.STYLE_SUCCESS)
							.setContent("密码修改成功")
							.setSubmitText("前往登录")
							.setOnSubmitListener(new TipsDialog.OnSubmitListener() {
								@Override
								public void onClick(TipsDialog dialog) {
									dialog.dismiss();
									finish();
								}
							})
							.build()
							.show();
				} else {
					TipsDialogUtil.commonDialogV2(mContext, response.getStatus());
				}

			}
		});

	}


	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_show2, R.anim.activity_hide2);
	}

}
