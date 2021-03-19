package com.xz.xlogin.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseFragment;
import com.xz.xlogin.widget.TimeButton;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class RegisterByPhoneFragment extends BaseFragment {

	private EditText etPhone;
	private EditText etCode;
	private LinearLayout registerTypeView;
	private TextView registerTxt;
	private ImageView registerImg;
	private TimeButton btnTime;
	private View.OnClickListener mOnClickListener;
	private int registerType = 1;//1手机注册 2邮箱注册

	@Override
	protected int getLayout() {
		return R.layout.fragment_register_phone;
	}

	@Override
	protected void initView(View rootView) {
		etPhone = rootView.findViewById(R.id.et_phone);
		etCode = rootView.findViewById(R.id.et_code);
		registerTypeView = rootView.findViewById(R.id.register_type);
		registerTxt = rootView.findViewById(R.id.register_txt);
		registerImg = rootView.findViewById(R.id.register_img);
		btnTime = rootView.findViewById(R.id.btn_time);
		btnTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etPhone.getText().toString().trim().equals("")) {
					sToast("请先输入手机号");
				} else {
					mOnClickListener.onClick(v);
				}
			}
		});
		registerTypeView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registerType == 1) {
					registerType = 2;
					registerTxt.setText("切换至手机注册");
					registerImg.setImageResource(R.mipmap.ic_phone);
					etPhone.setHint("邮箱号");
				} else if (registerType == 2) {
					registerType = 1;
					registerTxt.setText("切换至邮箱注册");
					registerImg.setImageResource(R.mipmap.ic_email);
					etPhone.setHint("手机号");
				}
			}
		});
	}


	@Override
	protected void initDate(Context mContext) {
		etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etPhone.setBackgroundResource(R.drawable.select_edit);
				}
			}
		});
	}


	/**
	 * 设置view默认状态
	 *
	 * @param view
	 */
	private void setNormalState(View view) {
		view.setBackgroundResource(R.drawable.select_edit);
	}

	/**
	 * 设置view错误状态
	 *
	 * @param view
	 */
	private void setErrorState(View view) {
		view.setBackgroundResource(R.drawable.select_edit_error);
	}

	/**
	 * ------------获取控件数值-------------
	 */

	public void startClock() {
		btnTime.start();
	}

	public void setTimeButtonClickListener(View.OnClickListener listener) {
		mOnClickListener = listener;
	}

	public String getPhone() {
		String phone = etPhone.getText().toString().trim();
		if (phone.equals("")) {
			etPhone.setBackgroundResource(R.drawable.select_edit_error);
			return "";
		}
		return phone;
	}

	public String getCode() {
		return etCode.getText().toString().trim();
	}


	public void cleanAll() {
		etCode.setText("");
		etPhone.setText("");
	}

}
