package com.xz.xlogin.widget;

import android.content.Context;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseDialog;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/16
 */
public class VerificationDialog extends BaseDialog {

	public VerificationDialog(Context context) {
		this(context, 0);
	}

	public VerificationDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.dialog_verification;
	}

	@Override
	protected void initData() {

	}
}
