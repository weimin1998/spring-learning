package com.example.springheima.returnValue;

import com.example.springheima.returnValue.Config.ReturnValController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.*;
import org.springframework.web.util.UrlPathHelper;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        returnResponseBody(applicationContext);

    }


    private static void returnmav(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test1 = ReturnValController.class.getMethod("test1");

        ReturnValController returnValController = new ReturnValController();
        Object result = test1.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        // 1.测试返回 mav
        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test1);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
        }
    }

    private static void returnStrViewName(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test2 = ReturnValController.class.getMethod("test2");

        ReturnValController returnValController = new ReturnValController();
        Object result = test2.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), new MockHttpServletResponse());

        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test2);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
        }
    }

    private static void returnModelAttributeAnnotation(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test3 = ReturnValController.class.getMethod("test3");

        ReturnValController returnValController = new ReturnValController();
        Object result = test3.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("/test3");
        UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(mockHttpServletRequest);
        ServletWebRequest request = new ServletWebRequest(mockHttpServletRequest, new MockHttpServletResponse());
        // 1.测试返回 mav
        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test3);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
        }
    }

    private static void returnHttpEntity(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test5 = ReturnValController.class.getMethod("test5");

        ReturnValController returnValController = new ReturnValController();
        Object result = test5.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), mockHttpServletResponse);
        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test5);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            if (!modelAndViewContainer.isRequestHandled()) {
                renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
            }

            System.out.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
        }
    }

    private static void returnHttpHeaders(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test6 = ReturnValController.class.getMethod("test6");

        ReturnValController returnValController = new ReturnValController();
        Object result = test6.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), mockHttpServletResponse);
        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test6);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            if (!modelAndViewContainer.isRequestHandled()) {
                renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
            }

            Collection<String> headerNames = mockHttpServletResponse.getHeaderNames();
            for (String headerName : headerNames) {
                System.out.println(headerName + ":" + mockHttpServletResponse.getHeader(headerName));
            }
        }
    }

    private static void returnResponseBody(AnnotationConfigApplicationContext applicationContext) throws Exception {
        Method test7 = ReturnValController.class.getMethod("test7");

        ReturnValController returnValController = new ReturnValController();
        Object result = test7.invoke(returnValController);

        HandlerMethodReturnValueHandlerComposite composite = getReturnValHandler();
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest(), mockHttpServletResponse);
        HandlerMethod handlerMethod = new HandlerMethod(returnValController, test7);
        if (composite.supportsReturnType(handlerMethod.getReturnType())) {
            composite.handleReturnValue(result, handlerMethod.getReturnType(), modelAndViewContainer, request);
            System.out.println(modelAndViewContainer.getModel());
            System.out.println(modelAndViewContainer.getViewName());

            if (!modelAndViewContainer.isRequestHandled()) {
                renderView(applicationContext, modelAndViewContainer, request);// 渲染视图
            }

            Collection<String> headerNames = mockHttpServletResponse.getHeaderNames();
            for (String headerName : headerNames) {
                System.out.println(headerName + ":" + mockHttpServletResponse.getHeader(headerName));
            }
            System.out.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
        }
    }

    private static void renderView(AnnotationConfigApplicationContext applicationContext, ModelAndViewContainer modelAndViewContainer, ServletWebRequest request) {

        ViewResolver viewResolver = applicationContext.getBean(ViewResolver.class);

        try {
            View view = viewResolver.resolveViewName(modelAndViewContainer.getViewName(), Locale.CHINESE);
            view.render(modelAndViewContainer.getModel(), request.getRequest(), request.getResponse());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static HandlerMethodReturnValueHandlerComposite getReturnValHandler() {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
        composite.addHandler(new RequestResponseBodyMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }
}
