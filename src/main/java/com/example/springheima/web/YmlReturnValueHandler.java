package com.example.springheima.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;

public class YmlReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(Yml.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        String str = new Yaml().dump(returnValue);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print(str);

        mavContainer.setRequestHandled(true);
    }
}
