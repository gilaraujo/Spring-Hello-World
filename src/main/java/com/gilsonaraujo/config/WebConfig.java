package com.gilsonaraujo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This is the Spring MVC configuration (in here as opposed to in the older xml format).
 *
 * @author gilson.araujo
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages =  {"com.gilsonaraujo"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pages/**").addResourceLocations("/pages/**");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/**");
    }
}
