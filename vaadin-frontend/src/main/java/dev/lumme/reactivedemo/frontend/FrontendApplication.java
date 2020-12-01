package dev.lumme.reactivedemo.frontend;

import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class FrontendApplication extends SpringBootServletInitializer {

    private static final Logger logger = LogManager.getLogger(FrontendApplication.class);

    public static void main(String[] args) throws Exception {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(FrontendApplication.class, args));
    }

    @Bean
    public NioEventLoopGroup nioEventLoopGroup() {
        return new NioEventLoopGroup(1);
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory(NioEventLoopGroup eventLoopGroup) {
        ReactorResourceFactory f= new ReactorResourceFactory();
        f.setLoopResources(b -> eventLoopGroup);
        f.setUseGlobalResources(false);
        return f;
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory r) {
        return new ReactorClientHttpConnector(r, m -> m);
    }

    @Bean
    public WebClient webClient(ReactorClientHttpConnector r) {
        return WebClient.builder().baseUrl("http://localhost:8082").clientConnector(r).build();
    }

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> {
            servletContext.setInitParameter("org.atmosphere.cpr.AtmosphereConfig.getInitParameter","true");
            servletContext.setInitParameter("org.atmosphere.cpr.broadcaster.shareableThreadPool", "true");
            servletContext.setInitParameter("org.atmosphere.cpr.broadcaster.maxProcessingThreads", "5");
            servletContext.setInitParameter("org.atmosphere.cpr.broadcaster.maxAsyncWriteThreads", "5");
        };
    }
}
