package com.example.springheima.argResolver;

import com.example.springheima.web.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);


        // 模拟一个request
        MultipartHttpServletRequest request = mockHttpServletRequest();
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);

        // 准备一个HandlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new ArgController(), ArgController.class.getMethod("args", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));

        // 数据绑定, 类型转换
        // @RequestParam int age,  String to int
        //DefaultDataBinderFactory defaultDataBinderFactory = new DefaultDataBinderFactory(null);
        ServletRequestDataBinderFactory defaultDataBinderFactory = new ServletRequestDataBinderFactory(null, null);

        // mavContainer，存储model中间结果
        ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();


        // 测试参数解析
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        for (MethodParameter methodParameter : methodParameters) {
            String annotations = Arrays.stream(methodParameter.getParameterAnnotations()).map(a -> a.annotationType().getSimpleName()).collect(Collectors.joining());
            String s = annotations.length() > 0 ? " @" + annotations + " " : " ";
            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            // useDefaultResolution： 表示必须有@RequestParam
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


            // 组合
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
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


            if (composite.supportsParameter(methodParameter)) {
                Object o = composite.resolveArgument(methodParameter, modelAndViewContainer, servletWebRequest, defaultDataBinderFactory);
                // System.out.println(o.getClass());
                System.out.println("[" + methodParameter.getParameterIndex() + "]" + s + methodParameter.getParameterType().getSimpleName() + " " + methodParameter.getParameterName() + "-->" + o);

                System.out.println("model data: "+modelAndViewContainer.getModel());
            } else {
                System.out.println("[" + methodParameter.getParameterIndex() + "]" + s + methodParameter.getParameterType().getSimpleName() + " " + methodParameter.getParameterName());
            }
        }
    }

    public static MultipartHttpServletRequest mockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("username", "zhangsan");
        request.setParameter("name", "zhangsan");
        request.setParameter("age", "22");
        request.setParameter("str", "test str");

        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
        System.out.println(map);
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);

        request.setContentType("application/json");

        request.setCookies(new Cookie("token", "123456"));

        request.setContent("{\"name\":\"lisi\",\"age\":20}".getBytes(StandardCharsets.UTF_8));

        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class ArgController {
        public void args(
                @RequestParam("username") String name,
                String str,
                @RequestParam int age,
                @RequestParam(value = "home", defaultValue = "${JAVA_HOME}") String home,
                @RequestParam("file") MultipartFile file,
                @PathVariable("id") int id,
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String homevalue,
                HttpServletRequest request,
                @ModelAttribute("abc") User user1, // 会将数据放到model中： abc=User{name='zhangsan', age=22}
                User user2, // 会将数据放到model中: user=User{name='zhangsan', age=22}
                @RequestBody User user3
        ) {

            /*
            *
            * =========all argumentResolvers===========
org.springframework.web.method.annotation.RequestParamMethodArgumentResolver@2a3c96e3
org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver@15cafec7
org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver@5b444398
org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver@cb191ca
org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver@42f48531
org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver@a776e
org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor@792bbc74
org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor@79145d5a
org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver@1f2f9244
org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver@4c4d27c8
org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver@6821ea29
org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver@338494fa
org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver@505a9d7c
org.springframework.web.servlet.mvc.method.annotation.SessionAttributeMethodArgumentResolver@758c83d8
org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver@129b4fe2
org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver@5af3a0f
org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver@19ae6bb
org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor@10993713
org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver@58359ebd
org.springframework.web.method.annotation.ModelMethodProcessor@24b6b8f6
org.springframework.web.method.annotation.MapMethodProcessor@72cf2de5
org.springframework.web.method.annotation.ErrorsMethodArgumentResolver@2bb7bd00
org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver@5f031ebd
org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver@4ee37ca3
com.example.springheima.web.TokenArgResolver@45c8d09f
org.springframework.web.servlet.mvc.method.annotation.PrincipalMethodArgumentResolver@53812a9b
org.springframework.web.method.annotation.RequestParamMethodArgumentResolver@14b030a0
org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor@41e350f1
            * */

        }
    }
}
