package org.sundart.dbjframework.Util;

public class StringUtils {
    public static String FirstLetterUppercase(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    public static String FirstLetterLowercase(String str){
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
