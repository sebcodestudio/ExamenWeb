package pe.com.hermes.controller;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("tableController")
@ViewScoped
public class TableController implements Serializable {

    private List<Usuario> usuarios;

    public TableController() {
        usuarios = Arrays.asList(
                new Usuario(1, "Pablo Castillo", "pablo@example.com"),
                new Usuario(2, "Ana Pérez", "ana@example.com"),
                new Usuario(3, "Luis Torres", "luis@example.com")
        );
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static class Usuario {
        private int id;
        private String nombre;
        private String correo;

        public Usuario(int id, String nombre, String correo) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getCorreo() { return correo; }
    }
}