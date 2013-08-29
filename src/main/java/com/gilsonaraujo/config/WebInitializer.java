package com.gilsonaraujo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * This java configuration replaces the /src/main/webapp/WEB-INF/web.xml
 * Java config is available with the Servlet 3.0 spec (and spring-web).
 *
 * @author gilson.araujo
 */

public class WebInitializer implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(WebInitializer.class);

    /**
     * environment property name.
     */
    public static final String ENV_PROP_NAME = "environment";

    /**
     * config web app context.
     *
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        final AnnotationConfigWebApplicationContext ctx =
                new AnnotationConfigWebApplicationContext();

        ctx.register(AppConfig.class);
        // set the active profiles from the 'environment' system property.
        if (ctx.getEnvironment().getProperty(ENV_PROP_NAME) == null) {
            LOG.info("!! Environment system property missing !! Setting to default 'local'");
            ctx.getEnvironment().setActiveProfiles("local");
        } else {
            ctx.getEnvironment().setActiveProfiles(ctx.getEnvironment().getProperty(ENV_PROP_NAME));
        }

        servletContext.setInitParameter(
                LogbackConfigListener.CONFIG_LOCATION_PARAM, "classpath:logback.xml");
        servletContext.addListener(new LogbackConfigListener());
        LOG.info("Env: " + ctx.getEnvironment().getProperty(ENV_PROP_NAME));

        servletContext.addListener(new ContextLoaderListener(ctx));
        configureSpringMvcServlet(servletContext);
    }


    /**
     * Config Spring MVC.
     *
     * @param servletContext
     */
    private void configureSpringMvcServlet(final ServletContext servletContext) {

        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebConfig.class);

        final ServletRegistration.Dynamic springMvcServlet =
                servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        springMvcServlet.setLoadOnStartup(2);
        springMvcServlet.addMapping("/*");
    }

}
