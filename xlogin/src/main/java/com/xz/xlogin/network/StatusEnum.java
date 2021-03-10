package com.xz.xlogin.network;

public enum StatusEnum {


	//===================基础信息（区间：-10~10）=====================
	SUCCESS(1, "success"),
	ERROR(-1, "未知错误"),
	//===================接口提示（区间：100~299）=====================
	STATUS_100(100, "当前已是最新版本"),
	STATUS_101(101, "更新文件错误"),
	STATUS_102(102, "请稍后重试"),
	//===================请求提示（区间：300~599）=====================
	STATUS_301(301, "签名验证失败"),
	STATUS_303(303, "时间未与服务器同步"),
	STATUS_304(304, "时间戳不合法"),
	STATUS_305(305, "缺少AppId参数"),
	STATUS_306(306, "非法AppId"),
	STATUS_307(307, "密钥无效"),
	STATUS_400(400, "参数错误"),
	STATUS_401(401, "缺失重要参数"),
	//===================用户账号（区间：600~799）=====================
	STATUS_600(600, "登录已过期"),
	STATUS_601(601, "账号或密码验证失败"),
	STATUS_602(602, "用户不存在"),
	STATUS_603(603, "用户信息更新失败"),
	STATUS_604(604, "用户创建失败"),
	STATUS_605(605, "用户更新失败"),
	STATUS_680(680, "旧密码错误"),
	STATUS_681(681, "手机号已注册"),
	STATUS_682(682, "邮箱已注册"),
	STATUS_683(683, "QQ已绑定账号"),
	STATUS_690(690, "qq未绑定账号"),
	STATUS_691(691, "邮箱未注册"),
	STATUS_692(692, "手机号未注册"),
	STATUS_693(693, "账号未注册");

	/**
	 * 响应状态码
	 */
	private final int code;
	/**
	 * 响应提示
	 */
	private final String msg;

	StatusEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static StatusEnum getEnum(int value) {
		StatusEnum[] enums = values();
		for (StatusEnum statusEnum : enums) {
			if (statusEnum.getCode().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static String getValue(Integer key) {
		StatusEnum[] businessModeEnums = values();
		for (StatusEnum businessModeEnum : businessModeEnums) {
			if (businessModeEnum.code == key) {
				return businessModeEnum.msg;
			}
		}
		return null;
	}
}
