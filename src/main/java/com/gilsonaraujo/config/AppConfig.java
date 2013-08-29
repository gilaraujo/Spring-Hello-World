package com.gilsonaraujo.config;

import org.springframework.context.annotation.ComponentScan;

/**
 * This is the Spring application configuration (in here as opposed to in the older xml format).
 *
 * @author gilson.araujo
 */
@ComponentScan(basePackages = {"com.gilsonaraujo.config.properties"})
public final class AppConfig {

    //private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

}
