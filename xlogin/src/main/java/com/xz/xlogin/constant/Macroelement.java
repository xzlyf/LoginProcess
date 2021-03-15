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
	public static String alt_appinfo = "/info";

	public static String BASE_URL_USER;
	public static String BASE_URL_INFO;

	//接口
	public static final String GET_USER_RULE = "/getUserRules";//获取隐私协议
	public static final String GET_REGISTER = "/register";//注册
	public static final String GET_LOGIN = "/login";//登录
	public static final String GET_LOGOUT = "/logout";//注销

	public static String appId;
	public static String appSecret;
	public static String version;

	//公钥
	//更新日期：2020/12/03 00:20
	public static String publicKey;


	//本地文件rsa公私密钥对
	//更新日期；2021/1/25 21:29
	public static final String localPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU_3BGP8MDqpw-PoEUSgGQEAQWfjm3qdFfqOdU8WuBCQI_9TvGWQzoOnqlHBwukkwf7atu4rQdP_0RbPvn6_c1wWI_LM6IZPE81XcldISn6nFc_2x9xHa2EPSluoj08xfhmD2ubYRO4RkZMAJ6Shi6ud_aoNCmp3GUzC1HbVCE_wIDAQAB";
	public static final String localPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJT_cEY_wwOqnD4-gRRKAZAQBBZ-Obep0V-o51Txa4EJAj_1O8ZZDOg6eqUcHC6STB_tq27itB0__RFs--fr9zXBYj8szohk8TzVdyV0hKfqcVz_bH3EdrYQ9KW6iPTzF-GYPa5thE7hGRkwAnpKGLq539qg0KancZTMLUdtUIT_AgMBAAECgYAsGedbg6fvP-J7Mfj2zmjkATmUhswCpssIK3A9Xi_Zw5bc43NL5DPD0PtYP5e_2SGbGbfyXDu_pJ79N2rgXKGQ3UDBcNBptQroGJV0oeg8x05NLM-614JKoONVOGJmAAq04VjjWw1FZuddPamLxySFsC9B07LhWMePR7E6X_2OwQJBANugSNnw2gLGbl0F228I_TeOQH4UeXfV63b5zat6SneLG6f0e_GREFOT93l8QSF7O0C25xjNxZQ_7AVulJvOR-8CQQCtrKcX2Y6IU1VaW9c9gc7CeNdCAvO1DlpiDOw6WWj3OZ5R3o3jl87SnFSdR-MNAK1qru3zDtJ0-Toj_WkwKQPxAkEAi_ZAlwtDlAMq8AM8jS04WcBA31hi9HiSXzbg2AfODxLKpCVNG2wr8GdajcQeqZyj7SDic08nO8vkuK147TXvZwJAA-dKhiW0p2nAFFzWFKLTQ9m1dN1eHuaor2V89b7Gti_MWCi5ZbEmy-MJn9NazhMC0tAFtx3RKyHi5aYvdA6n8QJBALgrU1hnMMtB76hjB9Z6BvNN1wt98wdza43TCjiyRKDtCkZ9Fx1pSEPAo-yUZ2sSrOwJh3lSPYIe068wOVKypDY";

	//用户账户
	public static String user;
	//用户token
	public static String token;
}
