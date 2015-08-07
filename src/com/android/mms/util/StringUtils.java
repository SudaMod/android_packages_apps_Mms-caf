package com.android.mms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2014/12/24 0024.
 */
public class StringUtils implements StaticObjectInterface {

    private StringUtils() {}

    /**
     * 判断字符串中时否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg || str.contains("【") || str.contains("】") || str.contains("。");
    }

    public static boolean isPersonalMoblieNO(String mobiles) {
        if(mobiles != null) {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            if (m == null) {
                return false;
            }
            else {
                return m.matches();
            }
        }
        return false;
    }

    public static String tryToGetCaptchas(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[a-zA-Z0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String mostLikelyCaptchas = "";
        int currentLevel = -1; //只有字母相似级别为0， 只有字母和数字可能级别为1, 只有数字可能级别为2.
        while (m.find()) {
            if (m.group().length() > 3 && m.group().length() < 8 && !m.group().contains(".")) {
                if(isNearToKeyWord(m.group(), str)) {
                    final String strr = m.group();
                    if(currentLevel == -1) {
                        mostLikelyCaptchas = m.group();
                    }
                    final int level = getLikelyLevel(m.group());
                    if(level > currentLevel) {
                        mostLikelyCaptchas = m.group();
                    }
                    currentLevel = level;
                }
            }
        }
        return mostLikelyCaptchas;
    }

    public static String tryToGetCaptchasEn(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        while (m.find()) {
            if (m.group().length() > 3 && m.group().length() < 8 && !m.group().contains(".")) {
                if(isNearToKeyWordEn(m.group(), str)) {
                    return m.group();
                }
            }
        }
        return "";
    }

    private static  int getLikelyLevel(String str) {
        if(str.matches("^[0-9]*$")) {
            return 2;
        } else if(str.matches("^[a-zA-Z]*$")) {
            return 0;
        } else {
            return 1;
        }

    }

    public static boolean isNearToKeyWordEn(String currentStr, String content) {
        int startPosition = 0;
        int endPosition = content.length() - 1;
        if (content.indexOf(currentStr) > 12) {
            startPosition = content.indexOf(currentStr) - 12;
        }
        if (content.indexOf(currentStr)  + currentStr.length() + 12 < content.length() - 1) {
            endPosition = content.indexOf(currentStr) + currentStr.length() + 12;
        }
        Boolean isNearToKeyWord = false;
        for (int i = 0; i < CPATCHAS_KEYWORD_EN.length; i++) {
            if (content.substring(startPosition, endPosition).contains(CPATCHAS_KEYWORD_EN[i])) {
                isNearToKeyWord = true;
                break;
            }
        }
        return isNearToKeyWord;
    }

    public static boolean isNearToKeyWord(String currentStr, String content) {
        int startPosition = 0;
        int endPosition = content.length() - 1;
        if (content.indexOf(currentStr) > 12) {
            startPosition = content.indexOf(currentStr) - 12;
        }
        if (content.indexOf(currentStr)  + currentStr.length() + 12 < content.length() - 1) {
            endPosition = content.indexOf(currentStr) + currentStr.length() + 12;
        }
        Boolean isNearToKeyWord = false;
        for (int i = 0; i < CPATCHAS_KEYWORD.length; i++) {
            if (content.substring(startPosition, endPosition).contains(CPATCHAS_KEYWORD[i])) {
                isNearToKeyWord = true;
                break;
            }
        }
        return isNearToKeyWord;
    }

    public static boolean isCaptchasMessage(String content) {
        Boolean isCaptchasMessage = false;
        for (int i = 0; i < CPATCHAS_KEYWORD.length; i++) {
            if (content.contains(CPATCHAS_KEYWORD[i])) {
                isCaptchasMessage = true;
                break;
            }
        }
        return isCaptchasMessage;
    }

    public static boolean isCaptchasMessageEn(String content) {
        Boolean isCaptchasMessage = false;
        for (int i = 0; i < CPATCHAS_KEYWORD_EN.length; i++) {
            if (content.contains(CPATCHAS_KEYWORD_EN[i])) {
                isCaptchasMessage = true;
                break;
            }
        }
        return isCaptchasMessage;
    }

}
