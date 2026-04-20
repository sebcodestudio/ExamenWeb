package pe.com.hermes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class ExamenWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamenWebApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		// Apuntar al directorio webapp para que Mojarra encuentre WEB-INF/faces-config.xml
		File webappDir = new File("src/main/webapp");
		if (webappDir.exists()) {
			factory.setDocumentRoot(webappDir);
		}
		return factory;
	}
}