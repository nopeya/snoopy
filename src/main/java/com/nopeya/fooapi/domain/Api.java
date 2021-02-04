package com.nopeya.fooapi.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Api {

    private Integer protocol;

    private String name;

    private String url;

    private String httpMethod;

    private String method;

    private String code;

    private String returnType;

    private List<Param> params = new ArrayList<>();

    @Data
    public static class Param {

        private Class<?> type;

        private String name;

        private Integer index;
    }
}

