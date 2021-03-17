package com.xz.xlogin.widget;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseDialog;
import com.xz.xlogin.constant.Macroelement;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/16
 */
public class VerificationDialog extends BaseDialog {

	private EditText etCode;
	private ImageView verifyImg;

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
		initView();
		Glide.with(getContext())
				.load(Macroelement.BASE_URL_APP + Macroelement.GET_VERIFY_IMG)
				.diskCacheStrategy(DiskCacheStrategy.NONE)
				.into(verifyImg);
		// TODO: 2021/3/17 完善验证码对话框
	}

	private void initView() {
		etCode = findViewById(R.id.et_code);
		verifyImg = findViewById(R.id.verify_img);
	}
}
