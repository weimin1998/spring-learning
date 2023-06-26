package com.example.springheima.mvcStep;

import com.example.springheima.mvcStep.Config.Controller1;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","zhangsan");

        ServletInvocableHandlerMethod servletInvocableHandlerMethod =
                new ServletInvocableHandlerMethod(new Controller1(), Controller1.class.getMethod("foo", Config.User.class));

        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(null,null);

        servletInvocableHandlerMethod.setDataBinderFactory(servletRequestDataBinderFactory);// 数据绑定
        servletInvocableHandlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());// 参数获取
        servletInvocableHandlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(applicationContext));


        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        servletInvocableHandlerMethod.invokeAndHandle(new ServletWebRequest(request), modelAndViewContainer);

        System.out.println(modelAndViewContainer.getModel());

        applicationContext.close();

    }

    public static HandlerMethodArgumentResolverComposite getArgumentResolvers(AnnotationConfigApplicationContext applicationContext){
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        RequestParamMethodArgumentResolver requestParamMethodArgumentResolver1 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), false);// 必须有@RequestParam
        RequestParamMethodArgumentResolver requestParamMethodArgumentResolver2 = new RequestParamMethodArgumentResolver(applicationContext.getBeanFactory(), true);
        PathVariableMethodArgumentResolver pathVariableMethodArgumentResolver = new PathVariableMethodArgumentResolver();
        RequestHeaderMethodArgumentResolver requestHeaderMethodArgumentResolver = new RequestHeaderMethodArgumentResolver(applicationContext.getBeanFactory());
        ServletCookieValueMethodArgumentResolver servletCookieValueMethodArgumentResolver = new ServletCookieValueMethodArgumentResolver(applicationContext.getBeanFactory());
        ExpressionValueMethodArgumentResolver expressionValueMethodArgumentResolver = new ExpressionValueMethodArgumentResolver(applicationContext.getBeanFactory());
        ServletRequestMethodArgumentResolver servletRequestMethodArgumentResolver = new ServletRequestMethodArgumentResolver();
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor1 = new ServletModelAttributeMethodProcessor(false);// 必须有@ModelAttribute
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor2 = new ServletModelAttributeMethodProcessor(true);
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = new RequestResponseBodyMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter()));

        composite.addResolvers(
                requestParamMethodArgumentResolver1,
                pathVariableMethodArgumentResolver,
                requestHeaderMethodArgumentResolver,
                servletCookieValueMethodArgumentResolver,
                expressionValueMethodArgumentResolver,
                servletRequestMethodArgumentResolver,
                servletModelAttributeMethodProcessor1,
                requestResponseBodyMethodProcessor,
                servletModelAttributeMethodProcessor2,
                requestParamMethodArgumentResolver2
        );

        return composite;

    }
}
