package com.software5000.util;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供一些对象有效性校验的方法
 */
public final class ValidUtil {


    /**
     * 英文字母 、数字和下划线
     */
    public final static Pattern GENERAL = Pattern.compile("^\\w+$");
    /**
     * 数字
     */
    public final static Pattern NUMBERS = Pattern.compile("\\d+");
    /**
     * 分组
     */
    public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
    /**
     * IP v4
     */
    public final static Pattern IPV4 = Pattern
            .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    /**
     * 货币
     */
    public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
    /**
     * 邮件
     */
    public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
    /**
     * 移动电话
     */
    public final static Pattern MOBILE = Pattern.compile("1\\d{10}");
    /**
     * 身份证号码
     */
    public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
    /**
     * 邮编
     */
    public final static Pattern ZIP_CODE = Pattern.compile("\\d{6}");
    /**
     * 生日
     */
    public final static Pattern BIRTHDAY = Pattern.compile("(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})日?$");
    /**
     * 中文字、英文字母、数字和下划线
     */
    public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");
    /**
     * UUID
     */
    public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
    /**
     * 不带横线的UUID
     */
    public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");

    /**
     * 正则表达式匹配中文
     */
    public final static String RE_CHINESE = "[\u4E00-\u9FFF]";

    /**
     * 国内固定电话
     */
    public static final String REG_FIXED_TELEPHONE = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";

    /**
     * 判断字符串是否是符合指定格式的时间
     *
     * @param date   时间字符串
     * @param format 时间格式
     * @return 是否符合
     */
    public final static boolean isDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断一个对象是否为空
     *
     * @param obj 对象
     * @return 是否为空
     */
    public final static boolean valid(Object obj) {
        return null != obj;
    }

    /**
     * 判断一组对象是否有效
     *
     * @param objs 对象数组
     * @return 是否为空
     */
    public final static boolean valid(Object... objs) {
        if (objs != null && objs.length != 0) {
            return true;
        }
        return false;
    }


    /**
     * 验证是否相等<br>
     * 当两值都为null返回true
     *
     * @param t1 对象1
     * @param t2 对象2
     * @return 当两值都为null或相等返回true
     */
    public static boolean equal(Object t1, Object t2) {
        return ObjectUtil.equal(t1, t2);
    }


    /**
     * 通过正则表达式验证
     *
     * @param regex 正则
     * @param value 值
     * @return 是否匹配正则
     */
    public static boolean isMatchRegex(String regex, String value) {
        if (value == null) {
            //提供null的字符串为不匹配
            return false;
        }
        if (isEmpty(regex)) {
            //正则不存在则为全匹配
            return true;
        }
        return Pattern.matches(regex, value);
    }


    /**
     * 通过正则表达式验证
     *
     * @param pattern 正则模式
     * @param value   值
     * @return 是否匹配正则
     */
    public static boolean isMatchRegex(Pattern pattern, String value) {
        if(value == null || pattern == null) {
            //提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(value).matches();
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     * @param value 值
     * @return 是否为英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value) {
        return isMatchRegex(GENERAL, value);
    }


    /**
     * 验证是否为给定长度范围的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min   最小长度，负数自动识别为0
     * @param max   最大长度，0或负数表示不限制最大长度
     * @return 是否为给定长度范围的英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value, int min, int max) {
        String reg = "^\\w{" + min + "," + max + "}$";
        if (min < 0) {
            min = 0;
        }
        if (max <= 0) {
            reg = "^\\w{" + min + ",}$";
        }
        return isMatchRegex(reg, value);
    }

    /**
     * 验证是否为给定最小长度的英文字母 、数字和下划线
     *
     * @param value 值
     * @param min   最小长度，负数自动识别为0
     * @return 是否为给定最小长度的英文字母 、数字和下划线
     */
    public static boolean isGeneral(String value, int min) {
        return isGeneral(value, min, 0);
    }

    /**
     * 验证该字符串是否是数字
     *
     * @param value 字符串内容
     * @return 是否是数字
     */
    public static boolean isNumber(String value) {
        if (isEmpty(value)) {
            return false;
        }
        return isMatchRegex(NUMBERS, value);
    }


    /**
     * 验证是否为货币
     *
     * @param value 值
     * @return 是否为货币
     */
    public static boolean isMoney(String value) {
        return isMatchRegex(MONEY, value);
    }


    /**
     * 验证是否为邮政编码（中国）
     *
     * @param value 值
     * @return 是否为邮政编码（中国）
     */
    public static boolean isZipCode(String value) {
        return isMatchRegex(ZIP_CODE, value);
    }


    /**
     * 验证是否为可用邮箱地址
     *
     * @param email 值
     * @return 否为可用邮箱地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        return isMatchRegex(EMAIL, email);
    }


    /**
     * 验证是否为手机号码（中国）
     *
     * @param value 值
     * @return 是否为手机号码（中国）
     */
    public static boolean isMobile(String value) {
        return isMatchRegex(MOBILE, value);
    }

    /**
     * 是否为国内电话
     *
     * @param str str
     * @return 是否正确
     */
    public static boolean isPhoneNum(String str) {
        return str.matches(REG_FIXED_TELEPHONE);
    }

    /**
     * 验证是否为身份证号码（18位中国）<br>
     * 出生日期只支持到到2999年
     *
     * @param value 值
     * @return 是否为身份证号码（18位中国）
     */
    public static boolean isCitizenId(String value) {
        return isMatchRegex(CITIZEN_ID, value);
    }


    /**
     * 验证是否为生日
     *
     * @param value 值 19900926
     * @return 是否为生日
     */
    public static boolean isBirthday(String value) {
        if (isMatchRegex(BIRTHDAY, value)) {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));

            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
                return false;
            }
            if (month == 2) {
                boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day > 29 || (day == 29 && !isleap)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 验证是否为IPV4地址
     *
     * @param value 值
     * @return 是否为IPV4地址
     */
    public static boolean isIpv4(String value) {
        return isMatchRegex(IPV4, value);
    }


    /**
     * 验证是否为URL
     *
     * @param value 值
     * @return 是否为URL
     */
    public static boolean isUrl(String value) {
        try {
            new java.net.URL(value);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }


    /**
     * 验证是否为汉字
     *
     * @param value 值
     * @return 是否为汉字
     */
    public static boolean isChinese(String value) {
        return isMatchRegex("^" + RE_CHINESE + "+$", value);
    }


    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     * @param value 值
     * @return 是否为中文字、英文字母、数字和下划线
     */
    public static boolean isGeneralWithChinese(String value) {
        return isMatchRegex(GENERAL_WITH_CHINESE, value);
    }


    /**
     * 验证是否为UUID<br>
     * 包括带横线标准格式和不带横线的简单模式
     *
     * @param value 值
     * @return 是否为UUID
     */
    public static boolean isUUID(String value) {
        return isMatchRegex(UUID, value) || isMatchRegex(UUID_SIMPLE, value);
    }

    /**
     * 判断是否纯字母组合
     *
     * @param src 源字符串
     * @return 是否纯字母组合的标志
     */
    public final static boolean isABC(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            final String regex = "^[a-z|A-Z]+$";
            Matcher m = Pattern.compile(regex).matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否浮点数字表示
     *
     * @param src 源字符串
     * @return 是否数字的标志
     */
    public final static boolean isFloatNumeric(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            final String regex = "^[0-9\\-\\.]+$";
            Matcher m = Pattern.compile(regex).matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 字符串是否为空
     *
     * @param s 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(Object s) {
        if (s == null || String.valueOf(s).trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 所有字符串是否为空
     *
     * @param objects 对象数组
     * @return 是否为空
     */
    public static boolean isAllEmpty(Object... objects) {
        for (Object o : objects) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 任意一个字符串是否为空
     *
     * @param objects 对象数组
     * @return 是否为空
     */
    public static boolean isAnyEmpty(Object... objects) {
        for (Object o : objects) {
            if (isEmpty(o)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串是否为非空
     *
     * @param o 被检测的字符串
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Object o) {
        return false == isEmpty(o);
    }

    /**
     * 是否为基本类型，包括包装类型和非包装类型
     *
     * @param object 被检查对象
     * @return 是否为基本类型
     * @see ClassUtil#isBasicType(Class)
     */
    public static boolean isBasicType(Object object) {
        return ClassUtil.isBasicType(object.getClass());
    }

    /**
     * 检查是否为有效的数字
     * 检查Double和Float是否为无限大，或者Not a Number
     * 非数字类型和Null将返回true
     *
     * @param obj 被检查类型
     * @return 检查结果，非数字类型和Null将返回true
     */
    public static boolean isValidIfNumber(Object obj) {
        if (obj != null && obj instanceof Number) {
            if (obj instanceof Double) {
                if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                    return false;
                }
            } else if (obj instanceof Float) {
                if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 比较值是否相等，如果参数有一个为null，则返回false
     * @param a 参数1
     * @param b 参数2
     * @return 是否相等
     */
    public static boolean equalsNoEmpty(String a,String b){
        if(a==null || b==null){
            return false;
        }else{
            return a.equals(b);
        }

    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
