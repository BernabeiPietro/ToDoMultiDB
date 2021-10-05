package com.example.todoappmultidb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.todoappmultidb.webcontroller.databinder.StringToMapConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMapConverter());
    }
}
