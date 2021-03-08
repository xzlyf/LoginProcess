package com.xz.xlogin.network;

public enum StatusEnum_old {
	SUCCESS(1, "success"),
	ERROR(-1, "未知错误"),
	//ERROR_TOKEN(2, "token已过期"),
	ERROR_TOKEN(2, "登录已过期"),
	ERROR_SIGN_NULL(3, "签名验证不存在"),
	ERROR_SIGN(4, "签名验证失败"),
	ERROR_TIMESTAMP_NULL(5, "时间戳不存在"),
	ERROR_TIMESTAMP(6, "时间未与服务器同步"),
	ERROR_TIMESTAMP_RECEIVE(7, "时间戳不合法"),
	ERROR_APPID_NULL(8, "缺失AppId参数"),
	ERROR_APPID_NOTFALL(9, "非法AppId"),
	ERROR_SECRET(10, "密钥无效"),
	ERROR_PARAMS(11, "参数错误"),
	FAILED_NULL_PARAMS(12, "缺失重要参数"),
	WORN_UPDATE_VERSION(66, "当前已是最新版本"),
	WORN_UPDATE_NULL(67, "更新文件错误"),
	NULL_USER(1045, "用户不存在"),
	FAILED_USER_ADD(1046, "用户创建失败"),
	FAILED_USER_UPDATE(1047, "用户更新失败"),
	FAILED_PHONE_EXIST(1048, "手机号已注册"),
	FAILED_USER_LOGIN(1049, "账号或密码验证失败"),
	FAILED_USER_LOGIN_NO_USER_PHONE(1050, "手机号未注册"),
	FAILED_USER_LOGIN_NO_USER_NO(1051, "账号未注册"),
	FAILED_USER_DETAIL_UPDATE(1052, "用户信息更新失败"),
	FAILED_EVENT_CREATE(1053, "事件创建失败"),
	FAILED_EVENT_DELETE(1054, "事件删除失败"),
	FAILED_EVENT_NULL(1055, "未找到对应事件"),
	FAILED_EVENT_UPDATE(1056, "更新事件失败"),
	FAILED_USER_OLDPWD(1057, "旧密码错误"),
	FAILED_EMAIL_EXIST(1058, "邮箱已注册"),
	FAILED_QQ_EXIST(1059, "QQ已绑定账号"),
	FAILED_USER_LOGIN_NO_USER_QQ(1060, "qq未绑定账号"),
	FAILED_USER_LOGIN_NO_USER_EMAIL(1061, "邮箱未注册"),
	FAILED_USER_LOGIN_NO_USER_ACCOUNT(1062, "账号未注册");

	/**
	 * 响应状态码
	 */
	private final int code;
	/**
	 * 响应提示
	 */
	private final String msg;

	StatusEnum_old(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static StatusEnum_old getEnum(int value) {
		StatusEnum_old[] enums = values();
		for (StatusEnum_old statusEnum : enums) {
			if (statusEnum.getCode().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static String getValue(Integer key) {
		StatusEnum_old[] businessModeEnums = values();
		for (StatusEnum_old businessModeEnum : businessModeEnums) {
			if (businessModeEnum.code == key) {
				return businessModeEnum.msg;
			}
		}
		return null;
	}
}
