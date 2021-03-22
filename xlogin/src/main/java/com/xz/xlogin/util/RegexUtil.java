package com.xz.xlogin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: xz
 * @Date: 2021/3/20
 * 常用正则表达式判断工具类
 */
public class RegexUtil {

    /**
     * 正则表达式：验证手机号(国内)
     */
    public static final String REGEX_MOBILE = "0?(13|14|15|18|17)[0-9]{9}";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 匹配正数
     */
    public static final String POSITIVE_NUMBER = "^(0\\.0*[1-9]+[0-9]*$|[1-9]+[0-9]*\\.[0-9]*[0-9]$|[1-9]+[0-9]*$)";

    /**
     * 匹配整数
     */
    public static final String INTEGER_REGEXP = "-?[1-9]\\d*";

    /**
     * 匹配正整数
     */
    public static final String POSITIVE_INTEGER_REGEXP = "[1-9]\\d*";

    /**
     * 匹配非负整数（正整数 + 0)
     */
    public static final String NON_NEGATIVE_INTEGERS_REGEXP = "^\\d+$";

    /**
     * 匹配负整数
     */
    public static final String NEGATIVE_INTEGERS_REGEXP = "-[1-9]\\d*";

    /**
     * 匹配非正整数（负整数 + 0）
     */
    public static final String NON_POSITIVE_INTEGERS_REGEXP = "";

    /**
     * 匹配正浮点数
     */
    public static final String POSITIVE_RATIONAL_NUMBERS_REGEXP = "[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*";

    /**
     * 匹配非负浮点数（正浮点数 + 0）
     */
    public static final String NON_NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^//d+(//.//d+)?$";

    /**
     * 匹配负浮点数
     */
    public static final String NEGATIVE_RATIONAL_NUMBERS_REGEXP = "-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)";

    /**
     * 匹配非正浮点数（负浮点数 + 0）
     */
    public static final String NON_POSITIVE_RATIONAL_NUMBERS_REGEXP = "^((-//d+(//.//d+)?)|(0+(//.0+)?))$";


    /**
     * 进行判断
     *
     * @param txt  待判断文本
     * @param rule 判断规则 参考RegexUtil的常量
     */
    public static boolean doRegex(String txt, String rule) {
        return Pattern.matches(rule, txt);
    }
}
