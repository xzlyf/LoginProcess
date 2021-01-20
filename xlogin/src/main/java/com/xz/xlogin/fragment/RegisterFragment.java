package com.xz.xlogin.fragment;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseFragment;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class RegisterFragment extends BaseFragment {

	//密码不符合规范
	private boolean isNotFit = false;
	private EditText etPhone;
	private EditText etCode;
	private EditText etPwd;
	private ImageView alterPwd;
	private EditText etRepeatPwd;
	private ImageView alter2;
	private TextView tvTips;

	@Override
	protected int getLayout() {
		return R.layout.fragment_register;
	}

	@Override
	protected void initView(View rootView) {

		etPhone = rootView.findViewById(R.id.et_phone);
		etCode = rootView.findViewById(R.id.et_code);
		etPwd = rootView.findViewById(R.id.et_pwd);
		alterPwd = rootView.findViewById(R.id.alter_pwd);
		etRepeatPwd = rootView.findViewById(R.id.et_repeat_pwd);
		alter2 = rootView.findViewById(R.id.alter_2);
		tvTips = rootView.findViewById(R.id.tv_tips);
	}

	@Override
	protected void initDate(Context mContext) {
		etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (!etRepeatPwd.getText().toString().equals("")) {
						sensePwd();
					}
				}
			}
		});

		etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etPhone.setBackgroundResource(R.drawable.select_edit);
				}
			}
		});
		etRepeatPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (!etPwd.getText().toString().equals("")) {
						sensePwd();
					}
				}
			}
		});
		alterPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPasswordEye(etPwd);
				setPasswordEye(etRepeatPwd);
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
		} else {
			//如果可见就设置为不可见
			editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		}
		//执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
		editText.setSelection(editText.getText().toString().length());
	}


	/**
	 * 判断两次密码是否一致
	 */
	private void sensePwd() {
		if (!etRepeatPwd.getText().toString().equals(etPwd.getText().toString())) {
			setErrorState(etRepeatPwd);
			setErrorState(etPwd);
			isNotFit = true;
		} else {
			setNormalState(etRepeatPwd);
			setNormalState(etPwd);
			isNotFit = false;
		}
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

	public String getPwd() {

		if (isNotFit) {
			setErrorState(etRepeatPwd);
			setErrorState(etPwd);
			tvTips.setText("两处密码不一致");
			return "";
		} else {
			tvTips.setText("");
		}

		String pwd = etPwd.getText().toString().trim();
		String rePwd = etRepeatPwd.getText().toString().trim();
		if (pwd.equals("")) {
			setErrorState(etPwd);
			return "";
		}

		if (rePwd.equals("")) {
			setErrorState(etRepeatPwd);
			return "";
		}
		if (rePwd.length() < 6) {
			setErrorState(etRepeatPwd);
			setErrorState(etPwd);
			tvTips.setText("密码长度不可以小于6位");
			return "";
		}


		return etRepeatPwd.getText().toString().trim();
	}


}
