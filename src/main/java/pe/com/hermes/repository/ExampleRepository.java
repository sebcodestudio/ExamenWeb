package pe.com.hermes.repository;

import org.springframework.stereotype.Repository;

@Repository
public class ExampleRepository {
    public String fetchData() {
        return "Datos desde el repositorio";
    }
}