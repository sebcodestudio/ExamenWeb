package pe.com.hermes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.com.hermes.model.Persona;
import pe.com.hermes.service.PersonaService;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final PersonaService personaService;

    public DataInitializer(PersonaService personaService) {
        this.personaService = personaService;
    }

    @Override
    public void run(String... args) {
        log.info("Cargando datos de prueba...");

        crearPersona("Carlos",    "Ramirez",   28, "carlos.ramirez@example.com");
        crearPersona("Maria",     "Lopez",      34, "maria.lopez@example.com");
        crearPersona("Jose",      "Gutierrez",  22, "jose.gutierrez@example.com");
        crearPersona("Ana",       "Torres",     45, "ana.torres@example.com");
        crearPersona("Luis",      "Mendoza",    31, "luis.mendoza@example.com");

        log.info("Datos de prueba cargados: {} personas", personaService.listarTodas().size());
    }

    private void crearPersona(String nombres, String apellidos, int edad, String correo) {
        if (!personaService.existeCorreo(correo, null)) {
            Persona p = new Persona();
            p.setNombres(nombres);
            p.setApellidos(apellidos);
            p.setEdad(edad);
            p.setCorreo(correo);
            personaService.guardar(p);
            log.debug("Persona creada: {} {}", nombres, apellidos);
        }
    }
}
