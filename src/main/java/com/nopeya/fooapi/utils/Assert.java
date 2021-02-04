package com.nopeya.fooapi.utils;

public class Assert {

    public static void isNotBlank(String str, String errMsg) {
        Assert.isTrue(StringUtils.isNotBlank(str), errMsg);
    }

    public static void failed(String errMsg) {
        Assert.isTrue(false, errMsg);
    }

    public static void isTrue(boolean expression, String errMsg) {
        if (!expression) {
            throw new AssertFailedException(errMsg);
        }
    }


}
