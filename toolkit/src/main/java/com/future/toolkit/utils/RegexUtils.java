package com.future.toolkit.utils;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by yangqc on 2019/9/23
 * 正则表达式
 * @Author yangqc
 */
public class RegexUtils {

    // http://tool.oschina.net/regex  检测正则
    // https://www.toutiao.com/i6231678548520731137 正则

    private static final String legalInputRegex = "[a-zA-Z0-9\\_\u4e00-\u9fa5]*";
    private static final String numberLetterLineRegex = "[a-zA-Z0-9\\_]*";
    private static final String REGEX_PHONE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String REGEX_PHONE_SIMPLE = "^[1]\\d{10}$";

    //判断是否是数字、字母、下划线、汉字
    public static boolean isInvalid(String str) {
        //unicode汉字编码范围：[0x4e00,0x9fa5]（或十进制[19968,40869]）
        Pattern pattern = compile(legalInputRegex);
        return pattern.matcher(str).matches();

    }

    //判断是否是数字、字母、下划线
    public static boolean isNumber0rLetterOrLine(String str) {
        Pattern pattern = compile(numberLetterLineRegex);
        return pattern.matcher(str).matches();
    }

    //手机正则
    public static boolean isMobilePhone(String input) {
        return isMatch(REGEX_PHONE, input) || isMatch(REGEX_PHONE_SIMPLE, input);

    }

    //邮箱正则
    public static boolean isEmail(String input) {
        return isMatch(REGEX_EMAIL, input);
    }

    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}