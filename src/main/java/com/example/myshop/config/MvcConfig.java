package com.example.myshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration      //этот класс содержит конфигурацию нашего веб слоя
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler ("/img/**")
                .addResourceLocations ("file:/" + uploadPath + "/");
    }

    public void addViewControllers (ViewControllerRegistry registry){
        registry.addViewController ("/setting").setViewName ("setting.html");
    }



    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerCustomizer(){
        return container -> {
            //добавили ErrorPage, если не находит даный адрес то перекидывает на "/"
            container.addErrorPages (new ErrorPage (HttpStatus.NOT_FOUND, "/"));
        };
    }

}
