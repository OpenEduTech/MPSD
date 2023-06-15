package com.example.poem.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决跨域问题
 */

@Configuration
public class CorsConfig {
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);


        // 设置你要允许的网站域名，如果全允许则设为 *
        config.addAllowedOrigin("*");

        // 如果要限制 HEADER 或 METHOD 请自行更改
        config.addAllowedHeader("*");

        config.addAllowedMethod("*");

        //暴露哪些头部信息(因为跨域访问默认不能获取全部头部信息)

/*        config.addExposedHeader("token");*//*暴露哪些头部信息 不能用*因为跨域访问默认不能获取全部头部信息*//*
        config.addExposedHeader("TOKEN");
        config.addExposedHeader("Authorization");*/

    //    config.addExposedHeader("authorization");

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

        // 这个顺序很重要哦，为避免麻烦请设置在最前
        bean.setOrder(0);
        return bean;
    }
}

