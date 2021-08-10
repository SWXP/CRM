package com.yjxxt.crm.config;

import com.yjxxt.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //指定拦截器,需要⼀个实现HandlerInterceptor接⼝的拦截器实例
        registry.addInterceptor(noLoginInterceptor())
                //添加过滤规则
                .addPathPatterns("/**")
                //添加放行规则
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
