package com.xz.xlogin.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseFragment;
import com.xz.xlogin.widget.TimeButton;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class RegisterByPhoneFragment extends BaseFragment {

	//密码不符合规范
	private boolean isNotFit = false;
	private EditText etPhone;
	private EditText etCode;
	private TimeButton btnTime;
	private View.OnClickListener mOnClickListener;

	@Override
	protected int getLayout() {
		return R.layout.fragment_register_phone;
	}

	@Override
	protected void initView(View rootView) {
		etPhone = rootView.findViewById(R.id.et_phone);
		etCode = rootView.findViewById(R.id.et_code);
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
