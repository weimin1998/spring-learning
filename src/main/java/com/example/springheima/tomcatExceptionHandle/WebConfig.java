package com.example.springheima.tomcatExceptionHandle;

import com.example.springheima.web.MyAdapter;
import com.example.springheima.web.TokenArgResolver;
import com.example.springheima.web.YmlReturnValueHandler;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
public class WebConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    // springmvc默认不会把RequestMappingHandlerMapping，RequestMappingHandlerAdapter放到容器中
    // 而是只作为DispatcherServlet的一个属性
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {

        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));

        return requestMappingHandlerAdapter;
    }


    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                // 可以是controller的方法
                // 也可以是一个页面，请求转发的方式；
                registry.addErrorPages(new ErrorPage("/error"));
            }
        };
    }
    @Bean
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor() {
        return new ErrorPageRegistrarBeanPostProcessor();
    }

    @Controller
    public static class MyController {

        @RequestMapping("test")
        public ModelAndView test() {
            int i = 1 / 0;
            return null;
        }

//        @RequestMapping("error")
//        @ResponseBody
//        public Map<String,Object> error(HttpServletRequest request){
//            Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//            Map<String,Object> map = new HashMap<>();
//            map.put("error",throwable.getMessage());
//            return map;
//        }
    }

    @Bean
    public BasicErrorController basicErrorController() {

        // 代表配置文件中的键值
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeException(true);
        // 第一个参数，表示要显示哪些信息
        return new BasicErrorController(new DefaultErrorAttributes(), errorProperties);
    }

    @Bean
    public View error() {
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                System.out.println(model);
                response.setContentType("text/html;charset=utf-8");

                response.getWriter().print("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "internal error" +
                        "\n" +
                        "</body>\n" +
                        "</html>");
            }
        };
    }

    @Bean
    public ViewResolver viewResolver(){
        return new BeanNameViewResolver();
    }
}
