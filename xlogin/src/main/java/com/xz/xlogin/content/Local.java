package com.xz.xlogin.content;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/12/9
 */

public class Local {

	public static String SERVER = "";//本地tomcat
	//public static String SERVER = "https://192.168.1.66";//IDEA spring boot 测试
	//public static String SERVER = "https://106.55.150.181";//腾讯云
	//public static String SERVER = "https://www.xzlyf.top";//域名访问 已有证书  默认端口：443 不用写
	//public static String SERVER = "https://www.xzlyf.top";//域名访问  默认端口：80 不用写
	public static String alt_user = "/user";
	public static String alt_todolist = "/todolist";
	public static String alt_appinfo = "/appinfo";

	public static String BASE_URL_USER = Local.SERVER + Local.alt_user;
	public static String BASE_URL_TODO = Local.SERVER + Local.alt_todolist;
	public static String BASE_URL_INFO = Local.SERVER + Local.alt_appinfo;

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
	//public static String token ="CDCBFBF02B9E9335FE203E92944B9DB3";
	public static String token;
}
