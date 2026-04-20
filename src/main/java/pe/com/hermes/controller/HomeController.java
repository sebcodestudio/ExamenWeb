package pe.com.hermes.controller;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named("homeController")
@ViewScoped
public class HomeController implements Serializable {
    private String message = "Bienvenido a PrimeFaces con Spring Boot";

    public String getMessage() {
        return "Hola desde el controlador!";
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

