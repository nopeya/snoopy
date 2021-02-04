package com.nopeya.fooapi.utils;

import groovy.lang.GroovyClassLoader;

public class GroovyClassUtils {

    public static Class forCode(String code) {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        System.out.println(code);
        Class<?> clazz = groovyClassLoader.parseClass(code);
        return clazz;
    }

}




