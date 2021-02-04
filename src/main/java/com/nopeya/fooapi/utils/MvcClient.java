package com.nopeya.fooapi.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class MvcClient  implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext applicationContext;
    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired(required = false)
    AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;
    @Autowired
    ConfigurableListableBeanFactory factory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MvcClient.applicationContext = applicationContext;
    }

    public Map<RequestMappingInfo, HandlerMethod> getAllRequestMapping() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        return handlerMethods;
    }

    public void register(String name, Object handler, Method method, String url, RequestMethod requestMethod) throws Exception {
        Class<?> handlerClass = handler.getClass();
        String handlerClassName = handlerClass.getName();

        /*构造请求信息*/
        // 请求http方法
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(requestMethod);
        // 请求地址
        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(url);
        // 请求参数限制
        ParamsRequestCondition paramsRequestCondition = new ParamsRequestCondition();
        // 请求头限制
        HeadersRequestCondition headersRequestCondition = new HeadersRequestCondition();
        // Content-Type类型限制
        ConsumesRequestCondition consumesRequestCondition = new ConsumesRequestCondition();
        // accept类型限制
        ProducesRequestCondition producesRequestCondition = new ProducesRequestCondition();
        // 支持的媒体类型
        MediaTypeExpression mediaTypeExpression = new MediaTypeExpression() {
            @Override
            public MediaType getMediaType() {
                return MediaType.APPLICATION_JSON;
            }

            @Override
            public boolean isNegated() {
                return false;
            }
        };
        producesRequestCondition.getExpressions().add(mediaTypeExpression);
        // 请求信息
        RequestMappingInfo requestMappingInfo
                = new RequestMappingInfo(name, patternsRequestCondition, requestMethodsRequestCondition, paramsRequestCondition, headersRequestCondition, consumesRequestCondition, producesRequestCondition, null);

        // 注入对象实例的依赖
        autowiredAnnotationBeanPostProcessor.processInjection(handler);
        // 注册对象实例到Ioc容器
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) factory;
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(handlerClass);
        if (!defaultListableBeanFactory.containsBeanDefinition(handlerClassName)) {
            defaultListableBeanFactory.registerBeanDefinition(handlerClassName, beanDefinitionBuilder.getBeanDefinition());
        }

        // 反射获取requestMappingHanderMapping方法,用来将新方法映射到mvc的请求列表里
        Class<? extends RequestMappingHandlerMapping> aClass = requestMappingHandlerMapping.getClass();
        Class<?> superclass = aClass.getSuperclass().getSuperclass();
        Method detectHandlerMethods = superclass.getDeclaredMethod("detectHandlerMethods", Object.class);
        detectHandlerMethods.setAccessible(true);
        detectHandlerMethods.invoke(requestMappingHandlerMapping, handlerClassName);

        // 已注册的话先注销
        Map<RequestMappingInfo, HandlerMethod> allRequestMapping = getAllRequestMapping();
        if (allRequestMapping.containsKey(requestMappingInfo)) {
            requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
        }
        requestMappingHandlerMapping.registerMapping(requestMappingInfo, handler, method);
    }
}
