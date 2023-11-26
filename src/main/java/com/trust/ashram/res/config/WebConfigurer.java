package com.trust.ashram.res.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
	// public static String uploadDirectory= "c:\\Trust_20210528\\Asharam_Image_Info";

	 public static String uploadDirectory2= "./src/main/resources/static/assets/images";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	 //registry.addResourceHandler("/Trust_20210528/Asharam_Image_Info/**").addResourceLocations("file:" + uploadDirectory+"\\");
    	 registry.addResourceHandler("/src/main/resources/static/assets/images/**").addResourceLocations("file:" + uploadDirectory2+"\\");
    	 
    }
}