package com.samrice.readingroomapi.config;

import com.samrice.readingroomapi.filters.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterFilterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter authFilter = new AuthFilter();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/api/shelves/*");
        return registrationBean;
    }

//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
//        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        source.registerCorsConfiguration("/**", config);
//        CorsFilter corsFilter = new CorsFilter(source);
//        registrationBean.setFilter(corsFilter);
//        registrationBean.setOrder(0);
//        return registrationBean;
//    }
}
