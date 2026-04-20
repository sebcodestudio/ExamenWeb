package pe.com.hermes.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.faces.webapp.FacesServlet;

@Configuration
public class JSFConfig {

    @Bean
    public ServletRegistrationBean<FacesServlet> facesServlet() {
        FacesServlet servlet = new FacesServlet();
        ServletRegistrationBean<FacesServlet> registration =
                new ServletRegistrationBean<>(servlet, "*.xhtml");
        registration.setLoadOnStartup(1);
        registration.setAsyncSupported(true);
        return registration;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addContextCustomizers(context -> {
            context.addParameter("com.sun.faces.forceLoadConfiguration", "true");
            context.addParameter("javax.faces.PROJECT_STAGE", "Development");
        });
    }
}