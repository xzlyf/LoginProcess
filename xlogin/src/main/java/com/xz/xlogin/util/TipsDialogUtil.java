package com.xz.xlogin.util;

import android.content.Context;

import com.xz.xlogin.widget.TipsDialog;


/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/6
 * 快速创建提示对话框
 * 对常用对话框进行整合简单处理
 */
public class TipsDialogUtil {

	/**
	 * 网络异常
	 */
	public static void badNetDialog(Context context) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_ERROR)
				.setTitle("Amm..(⊙﹏⊙)")
				.setContent("可恶，好像网络有点问题")
				.setSubmitText("那就稍后再试吧")
				.build()
				.show();
	}

	/**
	 * 不知道哪里出错了
	 */
	public static void errorDialog(Context context) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_ERROR)
				.setTitle("Emmm...出错了")
				.setContent("我也不知道哪里出问题了\n等等再试把/(ㄒoㄒ)/~~")
				.setSubmitText("等等再试")
				.build()
				.show();
	}

	/**
	 * 接口数据解析异常
	 */
	public static void systemErrorDialog(Context context) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_ERROR)
				.setTitle("Emmm...异常")
				.setContent("系统异常")
				.setSubmitText("那就稍后再试吧")
				.build()
				.show();
	}

	/**
	 * 接口数据解析异常
	 */
	public static void serverErrorDialog(Context context) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_ERROR)
				.setTitle("Emmm...")
				.setContent("服务器放飞自我了，请稍后再试吧")
				.setSubmitText("那好吧！")
				.build()
				.show();
	}

	/**
	 * 通用对话框
	 */
	public static void commonDialog(Context context, String text) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_WARN)
				.setTitle("Σ(っ °Д °;)っ")
				.setContent(text)
				.setSubmitText("让我想想")
				.build()
				.show();
	}

	public static void commonDialogV2(Context context, String text) {
		new TipsDialog.Builder(context)
				.setType(TipsDialog.STYLE_WARN)
				.setTitle("通知")
				.setContent(text)
				.setSubmitText("已知晓")
				.build()
				.show();
	}


}
