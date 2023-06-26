package com.example.springheima.returnValue;

import com.example.springheima.returnValue.Config.User;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageConvert {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        mockHttpServletRequest.addHeader("Accept","application/xml");
        mockHttpServletResponse.setContentType("application/json"); // 最高优先级

        ServletWebRequest servletWebRequest = new ServletWebRequest(mockHttpServletRequest, mockHttpServletResponse);

        // 如果不配置req 和resp
        // 默认按照这些message converter的先后顺序
        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(Arrays.asList(
                new MappingJackson2HttpMessageConverter(),
        new MappingJackson2XmlHttpMessageConverter()
        ));
        User user = new User();
        user.setName("zhangsan");
        processor.handleReturnValue(
               user,
                new MethodParameter(MessageConvert.class.getMethod("user"),-1),
                new ModelAndViewContainer(),
                servletWebRequest
        );

        System.out.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    @ResponseBody
    public User user(){
        return null;
    }
}
