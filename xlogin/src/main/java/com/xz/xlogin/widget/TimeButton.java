package com.xz.xlogin.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/3/15
 */
public class TimeButton extends Button implements View.OnClickListener {
	private String beforeText;
	private CountDownTimer countDownTimer;
	private int sec = 61;

	public TimeButton(Context context) {
		super(context);
		initView();
	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TimeButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}


	/**
	 * 初始化操作
	 */
	private void initView() {
		if (!TextUtils.isEmpty(getText())) {
			beforeText = getText().toString().trim();
		}
		this.setText(beforeText);
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		start();
	}

	public void setSec(int sec) {
		this.sec = sec;
	}


	public void start() {
		setEnabled(false);
		countDownTimer = new CountDownTimer(sec * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				TimeButton.this.setText(millisUntilFinished / 1000 + "秒");
			}

			@Override
			public void onFinish() {
				end();
			}
		};
		countDownTimer.start();
	}

	public void end() {
		setEnabled(true);
		setText(beforeText);
		countDownTimer.cancel();
	}


	@Override
	protected void onDetachedFromWindow() {
		end();
		super.onDetachedFromWindow();
	}
}
