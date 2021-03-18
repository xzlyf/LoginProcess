package com.xz.xlogin.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.signature.StringSignature;
import com.orhanobut.logger.Logger;
import com.xz.xlogin.R;
import com.xz.xlogin.api.CommonApi;
import com.xz.xlogin.base.BaseDialog;
import com.xz.xlogin.constant.Macroelement;
import com.xz.xlogin.network.NetUtil;

import java.io.InputStream;
import java.util.UUID;

import okhttp3.Request;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/16
 */
public class VerificationDialog extends BaseDialog {

	private EditText etCode;
	private ImageView verifyImg;
	private VerifyStatusCallback mCallback;
	private CommonApi api;

	public VerificationDialog(Context context) {
		this(context, 0);
	}

	private VerificationDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.dialog_verification;
	}

	@Override
	protected void initData() {
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		initView();
		if (mCallback == null) {
			throw new NullPointerException("请先监听验证码回调");
		}
		api = CommonApi.getInstance();
		mCallback.onStartVerify();
		showInputMethod();
		loadImg();
	}

	private void initView() {
		etCode = findViewById(R.id.et_code);
		verifyImg = findViewById(R.id.verify_img);
		etCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 4) {
					hideInputMethod();
					verifyCode(etCode.getText().toString().trim());
				}
			}
		});
		verifyImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadImg();
				mCallback.onRefreshImg();
			}
		});


	}

	/**
	 * 加载图片验证码
	 */
	private void loadImg() {
		Glide.with(getContext())
				.load(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_IMG)
				.signature(new StringSignature(UUID.randomUUID().toString()))  // 随机数字标签，方便下次请求更新图片
				.diskCacheStrategy(DiskCacheStrategy.NONE)
				.into(verifyImg);
	}

	/**
	 * 向后端校验验证码
	 */
	private void verifyCode(String code) {
		if (code == null) {
			return;
		}
		api.verifyCode(code, new NetUtil.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(String response) {
				Logger.w(response);
			}

		});
	}

	private void showInputMethod() {
		//主动弹出输入法
		etCode.post(new Runnable() {
			@Override
			public void run() {
				InputMethodManager im = (InputMethodManager) getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (im != null) {
					im.showSoftInput(etCode, InputMethodManager.RESULT_SHOWN);
				}
			}
		});
	}

	private void hideInputMethod() {
		//主动隐藏输入法
		etCode.clearFocus();

		etCode.post(new Runnable() {
			@Override
			public void run() {
				InputMethodManager im = (InputMethodManager) getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (im != null) {
					im.hideSoftInputFromWindow(etCode.getWindowToken(), 0); //强制隐藏键盘
				}
			}
		});

	}

	public void setVerifyStatusCallbackListener(VerifyStatusCallback callback) {
		this.mCallback = callback;
	}

	public interface VerifyStatusCallback {
		//打开对话框
		void onStartVerify();

		//刷新验证码
		void onRefreshImg();

		//验证过多
		void onVerifyOverMuch();

		//正在向后端验证
		void onVerifyIng();

		//验证成功
		void onVerifySuccess();

		//验证失败
		void onVerifyError();

	}
}
