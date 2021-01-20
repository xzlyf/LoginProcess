package com.xz.xlogin.constant;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/9
 */

public class Macroelement {

	public static String SERVER = "";//本地tomcat
	//public static String SERVER = "https://192.168.1.66";//IDEA spring boot 测试
	//public static String SERVER = "https://106.55.150.181";//腾讯云
	//public static String SERVER = "https://www.xzlyf.top";//域名访问 已有证书  默认端口：443 不用写
	//public static String SERVER = "https://www.xzlyf.top";//域名访问  默认端口：80 不用写
	public static String alt_user = "/user";
	public static String alt_todolist = "/todolist";
	public static String alt_appinfo = "/appinfo";

	public static String BASE_URL_USER;
	public static String BASE_URL_TODO;
	public static String BASE_URL_INFO;

	//接口
	public static final String GET_USER_RULE = "/getUserRules";//获取隐私协议
	public static final String GET_REGISTER = "/registerUser";//注册
	public static final String GET_LOGIN = "/login";//登录
	public static final String GET_EVENT = "/getDoneEvent";//获取事件

	public static String appId;
	public static String appSecret;
	public static String version;

	//公钥
	//更新日期：2020/12/03 00:20
	public static String publicKey;

	//用户token
	public static String token;
}
