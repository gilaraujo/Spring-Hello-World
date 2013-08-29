package com.gilsonaraujo.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * {@link ServletContextListener} that can be used in web applications to define the location of the logback
 * configuration. This is similar to Spring's {@link org.springframework.web.util.Log4jConfigListener}.
 *
 * <p>
 * Should be the first listener to configure logback before using it. Location is defined in the
 * <code>logbackConfigLocation</code> context param. Placeholders (ex: ${user.home}) are supported.<br/>
 * Location examples:<br />
 * /WEB-INF/log-sc.xml -> loaded from servlet context<br />
 * classpath:foo/log-cp.xml -> loaded from classpath<br />
 * file:/D:/log-absfile.xml -> loaded as url<br />
 * D:/log-absfile.xml -> loaded as absolute file<br />
 * log-relfile.xml -> loaded as file relative to the servlet container working directory<br />
 * </p>
 *
 * @author gilson.araujo
 */
public class LogbackConfigListener implements ServletContextListener {

    private static final String LOCATION_PREFIX_CLASSPATH = "classpath:";

    /**
     * The context parameter key name for where the logback.xml file is stored.
     */
    public static final String CONFIG_LOCATION_PARAM = "logbackConfigLocation";

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        final ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

        if (!(loggerFactory instanceof LoggerContext)) {
            servletContext.log("Can not configure logback. " + LoggerFactory.class + " is using " + loggerFactory
                    + " which is not an instance of " + LoggerContext.class);
            return;
        }

        final LoggerContext loggerContext = (LoggerContext) loggerFactory;

        String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);

        if (location != null) {
            location = OptionHelper.substVars(location, loggerContext);
        }

        if (location == null) {
            servletContext.log("Can not configure logback. Location is null." + " Maybe context param \""
                    + CONFIG_LOCATION_PARAM + "\" is not set or is not correct.");
            return;
        }

        final URL url = toUrl(servletContext, location);

        if (url == null) {
            servletContext.log("Can not configure logback. Could not find logback"
                    + " config neither as servlet context-, nor as classpath-, nor as url-, nor as file system"
                    + " resource. Config location = \"" + location + "\".");
            return;
        }

        servletContext.log("Configuring logback. Config location = \"" + location + "\", full url = \"" + url + "\".");

        configure(servletContext, url, loggerContext);
    }

    private void configure(
            final ServletContext servletContext, final URL location, final LoggerContext loggerContext) {
        final JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.stop();
        try {
            configurator.doConfigure(location);
        } catch (final JoranException e) {
            servletContext.log("Failed to configure logback.", e);
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

    private URL toUrl(final ServletContext servletContext, final String location) {
        URL url = null;

        if (location.charAt(0) == '/') {
            url = getConfigurationFromResource(servletContext, location);
        }

        if (url == null
                && location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
            url = Thread.currentThread().getContextClassLoader()
                    .getResource(location.substring(LOCATION_PREFIX_CLASSPATH.length()));
        }

        if (url == null) {
            url = getConfigurationFromURL(servletContext, location);
        }

        if (url == null) {
            url = getConfigurationFromFile(servletContext, location);
        }

        return url;
    }

    private URL getConfigurationFromFile(final ServletContext servletContext, final String location) {
        File file = new File(location);
        if (!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        if (file.isFile()) {
            try {
                return file.toURI().normalize().toURL();
            } catch (final MalformedURLException e) {
                servletContext.log("MalformedURLException:", e);
            }
        }
        return null;
    }

    private URL getConfigurationFromURL(final ServletContext servletContext, final String location) {
        try {
            return new URL(location);
        } catch (final MalformedURLException e) {
            servletContext.log("MalformedURLException:", e);
        }
        return null;
    }

    private URL getConfigurationFromResource(final ServletContext servletContext, final String location) {
        try {
            return servletContext.getResource(location);
        } catch (final MalformedURLException e) {
            servletContext.log("MalformedURLException:", e);
        }
        return null;
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        final ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

        if (!(loggerFactory instanceof LoggerContext)) {
            return;
        }

        final LoggerContext loggerContext = (LoggerContext) loggerFactory;
        loggerContext.stop();
    }
}
