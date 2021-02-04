package com.nopeya.fooapi.support;

import com.nopeya.fooapi.utils.Assert;
import com.nopeya.fooapi.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class POJOBuilder {

    private String packageName;

    private String className;

    private List<POJONode> fieldList = new ArrayList<>();

    public POJOBuilder putField(Type type, String fieldName) {
        POJONode fieldNode = new POJONode()
                .setBody("private " + type.getTypeName() + " " + fieldName + ";\n\n" +
                        getter(type, fieldName) +
                        setter(type, fieldName));
        fieldList.add(fieldNode);
        return this;
    }

    private String getter(Type type, String fieldName) {
        return "public " + type.getTypeName() + " get" + upFirstChar(fieldName) + "() {\n" +
                "\t" + "return this." + fieldName + ";\n" +
                "}\n\n";
    }

    private String setter(Type type, String fieldName) {
        return "public void set" + upFirstChar(fieldName) + "(" + type.getTypeName() + " " + fieldName + ") {\n" +
                "\tthis." + fieldName + " = " + fieldName + ";\n" +
                "}\n\n";
    }

    private String upFirstChar(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    private Predicate<String> stringNotBlank = s -> s != null && s.length() > 0;

    public String build() {
        Assert.isNotBlank(packageName, "包名不能为空");
        Assert.isNotBlank(className, "类名不能为空");

        POJONode clazzNode = new POJONode();

        POJONode packageNode = new POJONode();
        packageNode.setBody("package " + packageName + ";");

        POJONode clazzBody = new POJONode().setSeparator("\n")
                .setHead(new POJONode().setBody("public class " + className + " {"))
                .setBody(fieldList.stream().map(POJONode::toString).collect(Collectors.joining("\n")))
                .setTail(new POJONode().setBody("}"));

        clazzNode.setHead(packageNode)
                .setBody(clazzBody.toString());

        return clazzNode.toString();
    }

    public static void main(String[] args) {
        POJOBuilder builder = new POJOBuilder()
                .setPackageName("com.nopeya.api")
                .putField(Integer.class, "id")
                .putField(String.class, "name")
                .putField(Date.class, "time")
                .setClassName("Api");
        System.out.println(builder.build());
    }

    @Data
    @Accessors(chain = true)
    public class Field {

        private  String name;
        private Type type;
    }

}

@Data
@Accessors(chain = true)
class POJONode {

    static String SEPARATOR_ENTER = "\n";

    private POJONode head;
    private String body;
    private POJONode tail;

    private String separator = "\n";

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        if (null != head) {
            appendIfNotBlank(builder, head.toString(), separator);
        }
        appendIfNotBlank(builder, body, separator);
        if (null != tail) {
            appendIfNotBlank(builder, tail.toString(), separator);
        }

        return builder.toString();
    }

    private StringBuilder appendIfNotBlank(StringBuilder builder, String content, String separator) {
        if (StringUtils.isNotBlank(content)) {
            builder.append(content);
            appendIfNotBlank(builder, separator, null);
        }
        return builder;
    }

    public static void main(String[] args) {
        POJONode clazzNode = new POJONode();

        POJONode packageNode = new POJONode();
        packageNode.setBody("import com.example;");

        POJONode clazzBody = new POJONode().setSeparator("\n")
                .setHead(new POJONode().setBody("public class Model {"))
                .setBody("some code")
                .setTail(new POJONode().setBody("}"));

        clazzNode.setHead(packageNode)
                .setBody(clazzBody.toString());
        System.out.println(clazzNode);

    }

}


