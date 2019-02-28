package com.github.apycazo.snippets.jersey;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = JerseyApp.class)
public class JerseyApp extends ResourceConfig {

  private static final Resource[] resourceLocations = {
    new ClassPathResource("app.properties"),
    new ClassPathResource("app.yml"),
    new ClassPathResource("application.properties"),
    new ClassPathResource("application.yml"),
    new ClassPathResource("application-prod.properties"),
    new ClassPathResource("application-prod.yml"),
    new ClassPathResource("application-dev.properties"),
    new ClassPathResource("application-dev.yml"),
  };

  public static void main(String[] args) {
    SimpleCommandLinePropertySource props = new SimpleCommandLinePropertySource(args);
    int port = Integer.parseInt(Optional.ofNullable(props.getProperty("port")).orElse("8080"));
    Server server = configureServer(port);
    try {
      log.warn("Starting server on port: {}", port);
      server.start();
      server.join();
    } catch (Exception e) {
      log.error("Unable to start server", e);
    } finally {
      server.destroy();
    }
  }

  public static Server configureServer(int port) {
    Server server = new Server(port);
    String packageToScan = JerseyApp.class.getPackageName();
    // --- rest api requires no session
    ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
    // --- configure context path and context handler
    servletContextHandler.setContextPath("/");
    server.setHandler(servletContextHandler);
    // --- using resource config
    //    ServletHolder servletHolder = new ServletHolder(new ServletContainer(new JerseyConfig()));
    //    servletHolder.setInitOrder(0);
    //    servletContextHandler.addServlet(servletHolder, "/*");
    // --- without resource config
     ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
     servletHolder.setInitOrder(0);
     servletHolder.setInitParameter("jersey.config.server.provider.packages", packageToScan);
    // --- add spring support
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.registerShutdownHook();
    context.scan(packageToScan);
    // context.setConfigLocation(SpringConfig.class.getPackage().getName());
    servletContextHandler.addEventListener(new ContextLoaderListener(context));
    // --- done
    return server;
  }

  public JerseyApp() {
    log.info("Registering jersey resources");
    packages(JerseyApp.class.getPackage().getName());
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer () {
    PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
    Resource firstMatch = Arrays.stream(resourceLocations).filter(Resource::exists).findFirst().orElse(null);
    if (firstMatch != null && firstMatch.getFilename() != null) {
      Resource [] resources = Arrays.stream(resourceLocations).filter(Resource::exists).toArray(Resource[]::new);
      if (firstMatch.getFilename().endsWith(".properties")) {
        // load resources as properties
        ppc.setIgnoreResourceNotFound(true);
        ppc.setLocations(resourceLocations);
        ppc.setIgnoreResourceNotFound(true);
      } else {
        // load resources as yaml
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResolutionMethod(YamlProcessor.ResolutionMethod.OVERRIDE);
        factory.setResources(resources);
        factory.setSingleton(true);
        factory.afterPropertiesSet();
        ppc.setProperties(factory.getObject());
      }
    }

    return ppc;
  }
}
