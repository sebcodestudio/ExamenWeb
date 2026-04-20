# ExamenWeb - Sistema de Gestion de Personas

Prueba Tecnica - Analista Programador  
Tecnologias: Java 8, Spring Boot 2.7.9, JSF 2.2 (Mojarra), PrimeFaces 8, JPA/Hibernate, H2

---

## Requisitos previos

- Java 8 (JDK 1.8)
- Maven 3.6 o superior
- IntelliJ IDEA (recomendado) o cualquier IDE compatible con Maven

---

## Instrucciones para ejecutar

### Opcion 1 - Desde la terminal

```bash
# 1. Clonar o descomprimir el proyecto
cd ExamenWeb

# 2. Compilar y ejecutar
mvn clean spring-boot:run
```

### Opcion 2 - Desde IntelliJ IDEA

1. Abrir IntelliJ IDEA
2. Seleccionar **File > Open** y elegir la carpeta `ExamenWeb`
3. Esperar que Maven descargue las dependencias
4. Abrir la clase `ExamenWebApplication.java`
5. Hacer clic derecho > **Run 'ExamenWebApplication'**

### Acceder a la aplicacion

Una vez iniciado, abrir el navegador en:

```
http://localhost:8080/ExamenWeb/pages/home.xhtml
```

La base de datos H2 se inicializa automaticamente con 5 personas de prueba.

### Consola H2 (opcional)

```
http://localhost:8080/ExamenWeb/h2-console
JDBC URL: jdbc:h2:mem:examendb
Usuario:  examendb
Password: examen123
```

---

## Decisiones tecnicas

### Arquitectura en capas

Se respeto estrictamente la separacion en capas:

- **Model** (`Persona.java`) - Entidad JPA con validaciones Bean Validation
- **Repository** (`PersonaRepository.java`) - Interface que extiende `JpaRepository`, sin logica de negocio
- **Service** (`PersonaService.java`) - Capa de negocio, unico punto de acceso al repositorio
- **Controller** (`PersonaController.java`) - ManagedBean de JSF, solo coordina la vista con el servicio

Los ManagedBeans no acceden directamente al repositorio, siguiendo las recomendaciones del enunciado.

### Integracion Spring Boot + JSF

La combinacion JSF + Spring Boot con Tomcat embebido requiere configuracion especifica:

- Se usa **Mojarra 2.2.20** en lugar de 2.3.x porque Mojarra 2.3 requiere un contenedor CDI obligatorio (como Weld), que no esta disponible en Tomcat embebido
- Los ManagedBeans usan `@Component` + `@SessionScope` de Spring en lugar de `@Named` + `@ViewScoped` de CDI, permitiendo la inyeccion de dependencias con `@Autowired`
- El `SpringBeanFacesELResolver` en `faces-config.xml` conecta el motor EL de JSF con el contexto de Spring

### Persistencia

- **H2 en memoria** con `ddl-auto: create-drop`: la base de datos se crea al iniciar y se elimina al cerrar, ideal para pruebas
- **JpaRepository**: provee CRUD completo sin codigo adicional; la busqueda personalizada usa `@Query` JPQL
- **Validacion de correo duplicado**: se verifica antes de guardar, tanto en creacion como en edicion

### Experiencia de usuario

- Dialogo modal para crear/editar (sin cambio de pagina)
- Confirmacion antes de eliminar
- Mensajes `p:growl` no invasivos para feedback al usuario
- Paginacion y ordenamiento de columnas con PrimeFaces
- Exportacion a Excel con Apache POI (descarga directa)
- Datos de prueba al iniciar via `DataInitializer`

---

## Estructura del proyecto

```
src/
  main/
    java/pe/com/hermes/
      config/
        JSFConfig.java          - Configuracion del FacesServlet
        DataInitializer.java    - Datos de prueba al arrancar
      controller/
        PersonaController.java  - ManagedBean principal
      model/
        Persona.java            - Entidad JPA
      repository/
        PersonaRepository.java  - Acceso a datos
      service/
        PersonaService.java     - Logica de negocio
      ExamenWebApplication.java - Clase principal Spring Boot
    resources/
      application.properties   - Configuracion H2 y JPA
    webapp/
      pages/
        home.xhtml              - Pagina de inicio
        personas.xhtml          - Gestion completa de personas
      WEB-INF/
        faces-config.xml        - Configuracion JSF + Spring EL
        web.xml                 - Parametros del contexto web
```
