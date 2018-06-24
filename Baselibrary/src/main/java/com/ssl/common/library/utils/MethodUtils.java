package com.ssl.common.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 与业务无关的公共方法
 *
 * @author shishaolong
 *         <p>
 *         主要功能:
 *         </p>
 *         <p>
 *         创建日期:2014-8-14
 *         </p>
 *         <p>
 *         修改人,修改日期,修改内容
 *         </p>
 */
public final class MethodUtils {

    /**
     * 根据value获取key
     *
     * @param statusMap
     * @param stStr
     * @return
     */
    public static <T> String getStatusId(HashMap<String, T> statusMap, String stStr) {
        if (stStr == null || statusMap == null) {
            return "";
        }

        Iterator<Entry<String, T>> it = statusMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, T> entry = it.next();
            if (stStr.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "";
    }


    /**
     * 获取最大长度为len的字符串
     *
     * @param str
     * @param len
     * @return
     */
    public static String getShowLenStr(String str, int len) {
        if (len <= 0)
            return str;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /***
     * Html标签替换
     *
     * @param s
     * @return
     */
    public static String guoHtml(String s) {
        if (!TextUtils.isEmpty(s)) {
            String str = s.replaceAll("<[.[^<]]*>", "");
            str = str.replaceAll("&nbsp;", "");
            return str;
        } else {
            return "";
        }
    }


    /***
     * 在url中拼接参数
     *
     * @param url
     * @param paramsStr
     * @return
     */
    public static String getNewUrl(String url, String paramsStr) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(paramsStr))
            return url;
        if (url.indexOf("?") != -1) {
            if (!url.endsWith("&")) {
                url += "&";
            }
        } else {
            url += "?";
        }
        url += paramsStr;
        return url;
    }


    /***
     * 添加参数替换
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String getNewUrl(String url, String key, String value) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) {
            return url;
        }
        int index = url.indexOf(key);
        if (index != -1) {
            int endIndex = url.indexOf("&", index);
            String oldValue = "";
            if (endIndex == -1) {
                oldValue = url.substring(index + (key + "=").length());
            } else {
                oldValue = url.substring(index + (key + "=").length(), endIndex);
            }

            url = url.replace(oldValue, value);
            return url;
        } else {
            return getNewUrl(url, key + "=" + value);
        }
    }


    /**
     * 获取url上的参数集合
     *
     * @param url
     * @return
     */
    public static Map<String, Object> getUrlParams(String url) {
        HashMap<String, Object> params = new HashMap<>();
        try {
            if (!TextUtils.isEmpty(url)) {
                int index = url.indexOf("?");
                if (index != -1) {
                    String str = url.substring(index + 1);
                    String[] ps = str.split("&");
                    if (ps != null && ps.length > 0) {
                        for (String row : ps) {
                            if (!TextUtils.isEmpty(row)) {
                                String[] rs = row.split("=");
                                if (rs != null && rs.length == 2) {
                                    params.put(rs[0], rs[1]);
                                }
                            }

                        }
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    /***
     * 在map中取数据，处理一些默认的错误
     *
     * @param info
     * @param key
     * @return
     */
    public static <T> T getValueFormMap(Map<String, Object> info, String key, T def) {

        T result = def;
        try {
            if (info == null || info.size() <= 0) {
                return result;
            }
            Object tem = info.get(key);
            if (tem == null) {
                return result;
            }
            String value = tem.toString();
            if (def.getClass().equals(Integer.class)) {
                try {
                    result = (T) Integer.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Long.class)) {
                try {
                    result = (T) Long.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Float.class)) {
                try {
                    result = (T) Float.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Double.class)) {
                try {
                    result = (T) Double.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(String.class)) {
                return (T) (tem + "");
            } else {
                return (T) tem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    /**
     * 是否只是中文
     *
     * @return
     */
    public static boolean isChinese(String str) {
        return str.matches("[\\u4e00-\\u9fa5]+");
    }


    /**
     * 是否是数字、字母、中文组成,除去部分合法字符
     *
     * @return
     */
    public static boolean isNormal(String str, String... extraStr) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (extraStr != null && extraStr.length > 0) {
            for (String tem
                    : extraStr) {
                str = str.replaceAll(tem, "");
            }


        }
        return str.matches("[A-Za-z0-9\\u4e00-\\u9fa5]*");
    }


    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        String reg = "^(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+$";
        return str.matches(reg);
    }

    /**
     * 是否是身份证
     *
     * @param str
     * @return
     */
    public static boolean isCardNo(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        String reg = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/";
        return str.matches(reg);
    }

    /**
     * 是否是电话号码
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        String reg = "^1[0-9]{10}$";
        return str.matches(reg);
    }

    /***
     * 将字符串转成对应的数字类型
     *
     * @param value
     * @param def
     * @return
     */

    public static <T> T transformNum(String value, T def) {
        T result = def;
        try {
            if (TextUtils.isEmpty(value)) {
                return result;
            }

            if (def.getClass().equals(Integer.class)) {
                try {
                    result = (T) Integer.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Long.class)) {
                try {
                    result = (T) Long.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Float.class)) {
                try {
                    result = (T) Float.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (def.getClass().equals(Double.class)) {
                try {
                    result = (T) Double.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 密码为6~16位字母数字
     *
     * @param str
     * @return
     */
    public static boolean isFitPassword(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        String reg = "[0-9a-zA-Z]{6,20}";
        return str.matches(reg);

    }

    /**
     * 密码为6~16位字母数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        String reg = "[0-9a-zA-Z]+";
        return str.matches(reg);

    }

    /**
     * 短信验证码
     *
     * @param str
     * @return
     */
    public static boolean isFitNote(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        String reg = "[0-9]{6}";
        return str.matches(reg);

    }

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStringNull(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;

    }


    public static String getMorny(String number) {
        int mornyInt = MethodUtils.transformNum(number, 0);
        if (mornyInt % 100 != 0) {
            return MethodUtils.formatDouble(mornyInt / 100.0, "");
        } else {
            return MethodUtils.formatDouble(mornyInt / 100.0, "#0");
        }


    }


    /**
     * 格式化double数据，保留对应的小数
     *
     * @param value
     * @param md
     * @return
     */
    public static String formatDouble(double value, String md) {
        try {
            if (TextUtils.isEmpty(md)) {
                md = "#0.00";
            }

            DecimalFormat df = new DecimalFormat(md);

            return df.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value + "";
    }

    /**
     * 根据最大长度 截取字符串；大于最大长度时，截取
     *
     * @param str
     * @param maxLen
     */
    public static String cutStr(String str, int maxLen) {
        if (isStringNull(str)) {
            return "";
        }
        if (str.length() <= maxLen) {
            return str;
        }

        return str.substring(0, maxLen);
    }

    /***
     * 对List进行拍重复操作
     *
     * @param preData 数据
     * @param keys    需要比较的map中的key数组
     * @return
     */
    public static ArrayList<HashMap<String, Object>> removeRepeat(ArrayList<HashMap<String, Object>> preData, String... keys) {
        try {
            HashMap<String, HashMap<String, Object>> map = new LinkedHashMap<String, HashMap<String, Object>>(preData.size());
            for (HashMap<String, Object> row : preData) {
                String key = "";

                if (keys == null || keys.length == 0) {
                    keys = row.keySet().toArray(new String[0]);
                }

                for (String value : keys) {
                    key = key + "_" + MethodUtils.getValueFormMap(row, value, "");
                }
                map.put(key, row);
            }

            preData.clear();
            preData.addAll(map.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return preData;
    }

    /**
     * 根据value获取key的值
     *
     * @param info
     * @param value
     * @return
     */
    public static <T> String getMapKeyByValue(HashMap<String, T> info, String value) {
        String val = "";
        if (info != null && info.size() > 0 && value != null) {
            Iterator<Entry<String, T>> it = info.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, T> entry = it.next();
                if (entry.getValue().equals(value)) {
                    val = entry.getKey();
                    break;
                }
            }
        }
        return val;
    }

    /***
     * 判断一个map是否为空
     *
     * @param map
     * @return
     */
    public static boolean mapIsEmpty(Map map) {
        return map == null || map.size() <= 0;

    }

    /***
     * 判断一个List是否为空
     *
     * @return
     */
    public static boolean listIsEmpty(List list) {
        return list == null || list.size() <= 0;

    }


    /***
     * 是否是空  同时不能是字符串null
     *
     * @return
     */
    public static boolean isNullOrStrNull(String str) {
        return TextUtils.isEmpty(str) || "null".equals(str.toLowerCase());
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014.06.14"）
     *
     * @param time
     * @return
     */
    public static String getTimeStampToDate(long time) {
        try {
            if (time != 0) {
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
                String times = sdr.format(new Date(time * 1000L));
                return times;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否安装了微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
