package com.nopeya.fooapi.support;

import com.nopeya.fooapi.domain.Api;
import com.nopeya.fooapi.support.rpc.Exporter;
import com.nopeya.fooapi.utils.CollectionUtils;
import com.nopeya.fooapi.utils.GroovyClassUtils;
import com.nopeya.fooapi.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class Handler implements Exporter {

    private String name;

    private Class<?> clazz;

    private Object instance;

    private Method method;

    private Class<?>[] paramTypes;

    private RequestMethod requestMethod;

    private Api api;

    public static Handler get(Api api) throws Exception {

        Class<?> clazz = forApi(api);
        Object instance = clazz.newInstance();
        Class<?>[] paramTypes = parseParamTypes(api);
        Method method = clazz.getDeclaredMethod(api.getName(), paramTypes);
        RequestMethod httpMethod = getHttpMethod(api.getHttpMethod());

        api.setMethod(method.toString());

        return new Handler().setName(api.getName())
                .setClazz(clazz)
                .setInstance(instance)
                .setMethod(method)
                .setParamTypes(paramTypes)
                .setApi(api)
                .setRequestMethod(httpMethod);
    }

    @Override
    public Object call(Object...params) {
        try {
            Object[] param = params;
            return method.invoke(instance, param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("handler execute failed!");
        }
    }

    /**
     * 获取参数类型列表
     */
    private static Class<?>[] parseParamTypes(Api api) {
        if (CollectionUtils.isNotEmpty(api.getParams())) {
            return api.getParams().stream().map(Api.Param::getType).toArray(Class[]::new);
        }
        return null;
    }

    private static RequestMethod getHttpMethod(String method) {
        if (StringUtils.isNotBlank(method)) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                if (requestMethod.name().equals(method)) {
                    return requestMethod;
                }
            }
        }
        return null;
    }

    public static Class<?> forApi(Api api) {
        String T = api.getReturnType();
        String params = "";
        if (CollectionUtils.isNotEmpty(api.getParams())) {
            params = api.getParams().stream().map(p -> "@RequestParam(value=\"" + p.getName() + "\")  " + p.getType().getName() + " " + p.getName()).collect(Collectors.joining(", "));
        }
        String code =
                "import com.nopeya.fooapi.utils.*;\n" +
                        "import com.nopeya.fooapi.support.*;\n" +
                        "import org.springframework.web.bind.annotation.*;\n" +
                        "public class ScriptTemp {\n" +
                        "   @ResponseBody\n" +
                        "   public " + T + " " + api.getName() + "(" + params + ") {\n" +
                        api.getCode() + "\n" +
                        "   }\n" +
                        "}\n";
        return GroovyClassUtils.forCode(code);
    }

}
