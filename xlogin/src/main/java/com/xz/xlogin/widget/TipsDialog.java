package com.xz.xlogin.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.xlogin.R;
import com.xz.xlogin.base.BaseDialog;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/5
 */
public class TipsDialog extends BaseDialog {
	private LinearLayout rootView;
	private TextView tvTitle;
	private ImageView imgCancel;
	private ImageView imgType;
	private TextView tvContent;
	private TextView btnSubmit;

	/**
	 * 样式
	 */
	public static final int STYLE_SUCCESS = 0x0001;
	public static final int STYLE_GENERAL = 0x0002;
	public static final int STYLE_WARN = 0x0003;
	public static final int STYLE_ERROR = 0x0004;

	/**
	 * 对话框内容
	 */
	private String title = "请替换";
	private String content = "请替换";
	private String submitSt = "submit";
	/**
	 * 类型
	 */
	private int type;
	/**
	 * 信息回调
	 */
	private OnSubmitListener onSubmitListener;
	private OnCancelListener onCancelListener;


	private TipsDialog(Context context) {
		this(context, 0);
	}

	private TipsDialog(Context context, int themeResId) {
		super(context, themeResId);
	}


	@Override
	protected int getLayoutResource() {
		return R.layout.dialog_tips;
	}

	@Override
	protected void initData() {
		setCanceledOnTouchOutside(true);
		setCancelable(true);
		initView();
	}

	private void initView() {
		rootView = findViewById(R.id.root_view);
		tvTitle = findViewById(R.id.tv_title);
		imgCancel = findViewById(R.id.img_cancel);
		imgType = findViewById(R.id.img_type);
		tvContent = findViewById(R.id.tv_content);
		btnSubmit = findViewById(R.id.btn_submit);
		imgCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onCancelListener != null) {
					onCancelListener.onCancel(TipsDialog.this);
				}
				TipsDialog.this.cancel();
			}
		});
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onSubmitListener != null) {
					onSubmitListener.onClick(TipsDialog.this);
				}
				TipsDialog.this.cancel();
			}
		});
	}

	@Override
	public void show() {
		switch (type) {
			case TipsDialog.STYLE_GENERAL:
				generalType();
				break;
			case TipsDialog.STYLE_SUCCESS:
				successType();
				break;
			case TipsDialog.STYLE_WARN:
				warnType();
				break;
			case TipsDialog.STYLE_ERROR:
				errorType();
				break;
		}
		tvTitle.setText(title);
		tvContent.setText(content);
		btnSubmit.setText(submitSt);
		super.show();
	}

	private void generalType() {
		rootView.setBackgroundResource(R.drawable.share_round_general);
		btnSubmit.setBackgroundResource(R.drawable.select_dialog_general);
		imgCancel.setColorFilter(getContext().getColor(R.color.yellow_5));
		imgType.setImageResource(R.mipmap.ic_mark_smile);
		tvTitle.setTextColor(getContext().getColor(R.color.gray_6));
		tvContent.setTextColor(getContext().getColor(R.color.gray_6));
	}

	private void successType() {
		rootView.setBackgroundResource(R.drawable.share_round_success);
		btnSubmit.setBackgroundResource(R.drawable.select_dialog_success);
		imgCancel.setColorFilter(getContext().getColor(R.color.green_10));
		imgType.setImageResource(R.mipmap.ic_mark_success);

	}

	private void warnType() {
		rootView.setBackgroundResource(R.drawable.share_round_warn);
		btnSubmit.setBackgroundResource(R.drawable.select_dialog_warn);
		imgCancel.setColorFilter(getContext().getColor(R.color.yellow_10));
		imgType.setImageResource(R.mipmap.ic_mark_bulb);

	}

	private void errorType() {
		rootView.setBackgroundResource(R.drawable.share_round_error);
		btnSubmit.setBackgroundResource(R.drawable.select_dialog_error);
		imgCancel.setColorFilter(getContext().getColor(R.color.red_10));
		imgType.setImageResource(R.mipmap.ic_mark_amzing);
	}


	public static class Builder {
		private TipsDialog tipsDialog;

		public Builder(Context context) {
			tipsDialog = new TipsDialog(context);
		}

		/**
		 * 设置标题
		 */
		public Builder setTitle(String title) {
			tipsDialog.title = title;
			return this;
		}

		/**
		 * 设置内容
		 */
		public Builder setContent(String content) {
			tipsDialog.content = content;
			return this;
		}

		/**
		 * 按钮文本
		 */
		public Builder setSubmitText(String text) {
			tipsDialog.submitSt = text;
			return this;
		}

		/**
		 * 设置模式
		 * TipsDialog.TYPE_SUCCESS
		 * TipsDialog.TYPE_GENERAL
		 * TipsDialog.TYPE_WARN
		 * TipsDialog.TYPE_ERROR
		 */
		public Builder setType(int type) {
			tipsDialog.type = type;
			return this;
		}

		/**
		 * submit按键点击回调
		 */
		public Builder setOnSubmitListener(OnSubmitListener onSubmitListener) {
			tipsDialog.onSubmitListener = onSubmitListener;
			return this;
		}

		public Builder setOnCancelListener(OnCancelListener onCancelListener) {
			tipsDialog.onCancelListener = onCancelListener;
			return this;
		}

		public TipsDialog build() {
			tipsDialog.create();
			return tipsDialog;
		}
	}


	public interface OnSubmitListener {
		void onClick(TipsDialog dialog);
	}

	public interface OnCancelListener {
		void onCancel(TipsDialog dialog);
	}
}
