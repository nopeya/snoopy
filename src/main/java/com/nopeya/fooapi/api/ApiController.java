package com.nopeya.fooapi.api;

import com.nopeya.fooapi.domain.Api;
import com.nopeya.fooapi.support.Handler;
import com.nopeya.fooapi.support.Snoopy;
import com.nopeya.fooapi.utils.MvcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private MvcClient mvcClient;

    @GetMapping(value = "/hi")
    public ResponseEntity<String> hi() {
        return ResponseEntity.ok("hi, foo api!");
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Api api) throws Exception {

        Handler handler = Handler.get(api);
        Snoopy.push(handler);
        mvcClient.register(handler.getName(), handler.getInstance(), handler.getMethod(),
                handler.getApi().getUrl(), handler.getRequestMethod());

        return ResponseEntity.ok("wo hu ~ up fly!");
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity detail(@PathVariable String name) {
        Handler handler = Snoopy.get(name);
        return ResponseEntity.ok(handler.getApi());
    }

    @GetMapping(value = "/list")
    public ResponseEntity apiList() {
        Map<RequestMappingInfo, HandlerMethod> allRequestMapping = mvcClient.getAllRequestMapping();
        List<Api> list = new ArrayList();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : allRequestMapping.entrySet()) {

            RequestMappingInfo mappingInfo = entry.getKey();
            String handlerName = mappingInfo.getName();

            if (Snoopy.contains(handlerName)) {
                Handler handler = Snoopy.get(handlerName);
                list.add(handler.getApi());
            }

        }
        return ResponseEntity.ok(list);
    }

}
