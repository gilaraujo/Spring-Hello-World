package com.gilsonaraujo.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Application properties placeHolder for local environment.
 * @author gilson.araujo
 */
@Configuration
@Profile("local")
public class AppPropertiesLocal {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        final Resource[] resources = new ClassPathResource[]
                {new ClassPathResource("local/application.properties")};
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        pspc.setIgnoreResourceNotFound(true);
        return pspc;
    }

}
