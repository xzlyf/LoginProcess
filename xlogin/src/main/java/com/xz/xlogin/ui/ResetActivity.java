package com.xz.xlogin.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseActivity;

public class ResetActivity extends BaseActivity {

	private LinearLayout layoutSelect;
	private ImageView selectPhone;
	private ImageView selectEmail;
	private TextView btnSubmit;
	private LinearLayout layoutInput;
	private EditText etAccount;
	private EditText etCode;
	private EditText etPwd;
	private Button btnTime;


	private int mType = -1; //1手机号 2邮箱号

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
	}

	private void initView() {
		layoutSelect = findViewById(R.id.layout_select);
		selectPhone = findViewById(R.id.select_phone);
		selectEmail = findViewById(R.id.select_email);
		btnSubmit = findViewById(R.id.btn_submit);
		layoutSelect.setVisibility(View.VISIBLE);
		btnSubmit.setVisibility(View.INVISIBLE);
		btnSubmit.setEnabled(false);
		selectEmail.setOnClickListener(onSelectViewClick);
		selectPhone.setOnClickListener(onSelectViewClick);

		layoutInput = findViewById(R.id.layout_input);
		etAccount = findViewById(R.id.et_account);
		etCode = findViewById(R.id.et_code);
		etPwd = findViewById(R.id.et_pwd);
		btnTime = findViewById(R.id.btn_time);
		btnTime.setOnClickListener(onTimeClick);
		layoutInput.setVisibility(View.GONE);

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

	private View.OnClickListener onSelectViewClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			inputLayout();
			if (v.getId() == R.id.select_email) {
				mType = 2;
			} else if (v.getId() == R.id.select_phone) {
				mType = 1;
			}
		}
	};
	private View.OnClickListener onTimeClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};


	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_show2, R.anim.activity_hide2);
	}

}
