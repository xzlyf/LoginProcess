package com.xz.xlogin.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xz.utils.appUtils.ToastUtil;
import com.xz.utils.appUtils.TransparentBarUtil;
import com.xz.xlogin.R;
import com.xz.xlogin.util.ColorUtil;
import com.xz.xlogin.widget.LoadingDialog;
import com.xz.xlogin.widget.TipsDialog;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {
	protected final String TAG = this.getClass().getSimpleName();

	protected Activity mContext;
	protected TipsDialog tipsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());

		mContext = this;
		//设置是否开启返回homeAsUp按钮
		if (homeAsUpEnabled()) {
			ActionBar bar = getSupportActionBar();
			if (bar != null) {
				bar.setHomeButtonEnabled(true);
				bar.setDisplayHomeAsUpEnabled(true);

			}
		}

		initData();


	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public abstract boolean homeAsUpEnabled();

	public abstract int getLayoutResource();

	public abstract void initData();

	/**
	 * 权限结果回调
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == 456) {
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					//成功
				} else {
					//失败
				}
			}
			//给不给权限都给进了
			initData();

		}


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:  //id不要写错，前面要加android
				onBackPressed();
				break;
		}
		return true;
	}

	private LoadingDialog loadingDialog;

	@Override
	public void showLoading(String tips) {
		showLoading(tips, true, null);
	}

	@Override
	public void showLoading(String tips, boolean cancel, DialogInterface.OnCancelListener cancelListener) {
		if (loadingDialog == null || !loadingDialog.isShowing()) {
			loadingDialog = new LoadingDialog.Builder(mContext)
					.setTips(tips)
					.canExit(cancel)
					.build();
			loadingDialog.setOnCancelListener(cancelListener);
		}
		loadingDialog.show();
	}

	@Override
	public void disLoading() {
		if (loadingDialog != null) {
			if (loadingDialog.isShowing()) {
				loadingDialog.cancel();
			}
			loadingDialog = null;
		}

	}

	@Override
	public void sToast(String text) {
		ToastUtil.Shows(mContext, text);
	}

	@Override
	public void lToast(String text) {
		ToastUtil.Shows_LONG(mContext, text);
	}

	@Override
	public void sDialog(String title, String msg) {
		dDialog();
		tipsDialog = new TipsDialog.Builder(mContext)
				.setTitle(title)
				.setContent(msg)
				.setType(TipsDialog.STYLE_GENERAL)
				.build();
		tipsDialog.show();
	}

	@Override
	public void dDialog() {
		if (tipsDialog != null) {
			if (tipsDialog.isShowing()) {
				tipsDialog.dismiss();
			}
			tipsDialog = null;
		}
	}

	/**
	 * 切换日间夜间模式
	 *
	 * @param isNight true 夜间模式 false日间模式
	 */
	protected void changeMode(boolean isNight) {
		if (isNight) {
			// 夜间模式
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			// 日间模式
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
		//recreate();

		startActivity(new Intent(this, this.getClass()));
		overridePendingTransition(R.anim.transparen_show, R.anim.transparen_hide);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 800);
	}

	/**
	 * 判断当前是什么模式
	 *
	 * @return true 夜间模式 false 日间模式
	 */
	protected boolean isNightMode() {
		switch (AppCompatDelegate.getDefaultNightMode()) {
			case AppCompatDelegate.MODE_NIGHT_YES:
				return true;
			case AppCompatDelegate.MODE_NIGHT_NO:
			case AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY:
			case AppCompatDelegate.MODE_NIGHT_AUTO_TIME:
			case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
			case AppCompatDelegate.MODE_NIGHT_UNSPECIFIED:
			default:
				return false;
		}
	}

	/**
	 * 隐藏标题栏
	 */
	protected void hideActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}
	}

	/**
	 * 显示标题栏
	 */
	protected void showActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.show();
		}
	}

	/**
	 * 设置标题栏颜色
	 */
	protected void setActionBarColor(int color) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && actionBar.isShowing()) {
			actionBar.setBackgroundDrawable(new ColorDrawable(color));
		}
	}

	/**
	 * 设置标题栏标题颜色
	 * 基于html原理实现
	 */
	protected void setActionBarTitleColor(int color) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null && actionBar.isShowing()) {
			actionBar.setTitle(
					Html.fromHtml("<font color=\""
							+ ColorUtil.int2Hex(color)
							+ "\">"
							+ getTitle()
							+ "</font>"));
		}
	}

	/**
	 * 设置返回键颜色
	 */
	protected void setActionBarBackColor(int color) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) return;
		@SuppressLint("PrivateResource")
		Drawable upArrow = getDrawable(R.drawable.abc_ic_ab_back_material);
		if (upArrow == null) return;
		upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		actionBar.setHomeAsUpIndicator(upArrow);
	}


	/**
	 * 隐藏导航栏和底部虚拟按键
	 * 全屏
	 */
	protected void hideBottomMenu() {
		if (Build.VERSION.SDK_INT >= 19) {
			//for new api versions.
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

	/**
	 * 显示导航栏和底部虚拟按键
	 */
	protected void showBottomMenu() {
		if (Build.VERSION.SDK_INT >= 19) {
			View decorView = getWindow().getDecorView();
			int uiOptions = View.VISIBLE;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

	/**
	 * 隐藏状态栏
	 */
	protected void hideStatusBar() {
		TransparentBarUtil.makeStatusBarTransparent(this);
	}

	/**
	 * 显示状态栏并指定颜色
	 *
	 * @param color
	 */
	protected void showStatusBar(int color) {
		TransparentBarUtil.cleanStatusBarTransparent(this, color);
	}

	/**
	 * 改变状态栏字体颜色
	 */
	protected void changeStatusBarTextColor() {
		Window window = getWindow();
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			window.getDecorView().setSystemUiVisibility(View.VISIBLE);

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
				//透明底，黑色字
				window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
				window.setStatusBarColor(getResources().getColor(R.color.TRANSPARENT));

			} else {
				//黑色底，白色字
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
			}
		}

	}

	/**
	 * 点击编辑框外隐藏软键盘并清除焦点
	 *
	 * @param ev
	 * @return
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	/**
	 * 是否应该隐藏键盘
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = {0, 0};
			//获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				v.clearFocus();
				return true;
			}
		}
		return false;
	}


}
