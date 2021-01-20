package com.xz.xlogin.util;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/16
 */
public class ScreenUtil {

	/**
	 * 判断一个view是否在屏幕范围内 是否可见
	 */
	public static boolean isOnTheScreen(Activity activity, View view) {
		Point p = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(p);
		int screenWidth = p.x;
		int screenHeight = p.y;
		Rect rect = new Rect(0, 0, screenWidth, screenHeight);
		int[] location = new int[2];
		view.getLocationInWindow(location);
		if (view.getLocalVisibleRect(rect)) {
			// 控件在屏幕可见区域
			return true;
		} else {
			// 控件已不在屏幕可见区域（已滑出屏幕）
			return false;
		}

	}
}
