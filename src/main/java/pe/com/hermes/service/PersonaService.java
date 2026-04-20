package pe.com.hermes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.com.hermes.model.Persona;
import pe.com.hermes.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    private static final Logger log = LoggerFactory.getLogger(PersonaService.class);

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> listarTodas() {
        return personaRepository.findAll();
    }

    public void guardar(Persona persona) {
        log.debug("Guardando persona: {}", persona.getCorreo());
        personaRepository.save(persona);
    }

    public void eliminar(Long id) {
        log.debug("Eliminando persona con id: {}", id);
        personaRepository.deleteById(id);
    }

    public boolean existeCorreo(String correo, Long idActual) {
        Optional<Persona> existente = personaRepository.findByCorreo(correo);
        if (!existente.isPresent()) return false;
        // Si es edición del mismo registro, no cuenta como duplicado
        return !existente.get().getId().equals(idActual);
    }

    public List<Persona> buscar(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return listarTodas();
        }
        return personaRepository.buscarPorNombreOApellido(filtro.trim());
    }
}