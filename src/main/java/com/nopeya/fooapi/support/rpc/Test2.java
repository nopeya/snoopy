package com.nopeya.fooapi.support.rpc;

import com.nopeya.fooapi.domain.Api;
import com.nopeya.fooapi.support.Handler;
import com.nopeya.fooapi.support.Snoopy;

import java.util.ArrayList;
import java.util.List;

public class Test2 {

    public static void main(String[] args) throws Exception {
        back();
    }

    private static void back() throws Exception{
        List<Api.Param> paramList = new ArrayList<>();
        paramList.add(new Api.Param().setIndex(1).setName("a").setType(Integer.class));
        paramList.add(new Api.Param().setIndex(2).setName("b").setType(Integer.class));
        Api api = new Api().setName("add").setParams(paramList).setReturnType(Integer.class.getName())
                .setCode("return a + b;")
                .setProtocol(1)
                .setUrl("")
                .setHttpMethod("Get");
        Snoopy.push(Handler.get(api));

        HandlerImporter importer = new HandlerImporter();
        Exporter add = importer.importer("add");
        System.out.println(add.call(4, 6));
    }

}
