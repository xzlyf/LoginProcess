package com.xz.xlogin.ui.fragment;

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
public class RegisterByPwdFragment extends BaseFragment {

	private EditText etPwd;
	private ImageView alterPwd;
	private TextView tvTips;

	@Override
	protected int getLayout() {
		return R.layout.fragment_register_pwd;
	}

	@Override
	protected void initView(View rootView) {
		etPwd = rootView.findViewById(R.id.et_pwd);
		alterPwd = rootView.findViewById(R.id.alter_pwd);
		tvTips = rootView.findViewById(R.id.tv_tips);
	}

	@Override
	protected void initDate(Context mContext) {
		alterPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPasswordEye(etPwd);
			}
		});
		etPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setNormalState(etPwd);
				}
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


	/**
	 * 设置view默认状态
	 */
	private void setNormalState(View view) {
		view.setBackgroundResource(R.drawable.select_edit);
	}

	/**
	 * 设置view错误状态
	 */
	private void setErrorState(View view) {
		view.setBackgroundResource(R.drawable.select_edit_error);
	}

	/**
	 * ------------获取控件数值-------------
	 */

	public String getPwd() {
		String pwd = etPwd.getText().toString().trim();
		if (pwd.equals("")) {
			setErrorState(etPwd);
			return "";
		}
		if (pwd.length() < 6) {
			setErrorState(etPwd);
			setTips("密码强度若，密码不可少于6位");
			return "";
		} else if (pwd.length() > 16) {
			setErrorState(etPwd);
			setTips("密码过长");
			return "";
		}
		return pwd;
	}

	public void setTips(String msg) {
		tvTips.setText(msg);
	}

	public void cleanAll() {
		etPwd.setText("");
		tvTips.setText("");
	}

}
