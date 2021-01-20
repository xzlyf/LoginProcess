package com.xz.xlogin.fragment;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseFragment;


/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/10
 */
public class LoginFragment extends BaseFragment {

	//当前页面  1-账号登录 2-手机号登录 3-注册 4-忘记密码
	private int type = 1;
	private EditText etUser;
	private EditText etPwd;
	private TextView tvType;
	private TextView tvForget;

	@Override
	protected int getLayout() {
		return R.layout.fragment_login;
	}

	@Override
	protected void initView(View rootView) {
		etUser = rootView.findViewById(R.id.et_user);
		etPwd = rootView.findViewById(R.id.et_pwd);
		tvType = rootView.findViewById(R.id.tv_type);
		tvForget = rootView.findViewById(R.id.tv_forget);
		tvType.setOnClickListener(OnViewClickListener);
		tvForget.setOnClickListener(OnViewClickListener);
	}

	@Override
	protected void initDate(Context mContext) {
		etUser.setOnFocusChangeListener(focusChangeListener);
		etPwd.setOnFocusChangeListener(focusChangeListener);
	}

	private View.OnClickListener OnViewClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onViewClick(v);
		}
	};

	private void onViewClick(View view) {
		int id = view.getId();
		if (id == R.id.tv_type) {
			if (type == 1) {
				type = 2;
				tvType.setText("手机号登录");
			} else if (type == 2) {
				type = 1;
				tvType.setText("账号密码登录");
			}
		} else if (id == R.id.tv_forget) {
		}
	}

	private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				setNormalState(v);
			}
		}
	};

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
	 * /**
	 * --------开放接口---------
	 */
	public void setUserNo(String userNo) {
		etUser.setText(userNo);
		etPwd.setFocusable(true);
	}

	public String getUserNo() {
		String no = etUser.getText().toString().trim();
		if (no.equals("")) {
			setErrorState(etUser);
			return "";
		}
		return no;
	}

	public String getPwd() {
		String pwd = etPwd.getText().toString().trim();
		if (pwd.equals("")) {
			setErrorState(etPwd);
			return "";
		}
		return pwd;
	}

	public void setPwdError() {
		setErrorState(etPwd);
	}

}
