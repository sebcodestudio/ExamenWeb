package pe.com.hermes.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pe.com.hermes.model.Persona;
import pe.com.hermes.service.PersonaService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component("personaController")
@SessionScope
public class PersonaController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(PersonaController.class);

    @Autowired
    private PersonaService personaService;

    private List<Persona> personas = new ArrayList<>();
    private Persona personaSeleccionada = new Persona();
    private String filtro = "";
    private boolean modoEdicion = false;

    @PostConstruct
    public void init() {
        cargarPersonas();
    }

    public void cargarPersonas() {
        try {
            personas = personaService.listarTodas();
            log.debug("Personas cargadas: {}", personas.size());
        } catch (Exception e) {
            log.error("Error al cargar personas", e);
            personas = new ArrayList<>();
        }
    }

    public void prepararNueva() {
        personaSeleccionada = new Persona();
        modoEdicion = false;
    }

    public void prepararEdicion(Persona persona) {
        personaSeleccionada = new Persona();
        personaSeleccionada.setId(persona.getId());
        personaSeleccionada.setNombres(persona.getNombres());
        personaSeleccionada.setApellidos(persona.getApellidos());
        personaSeleccionada.setEdad(persona.getEdad());
        personaSeleccionada.setCorreo(persona.getCorreo());
        modoEdicion = true;
        log.debug("Editando persona id={}", persona.getId());
    }

    public void guardar() {
        try {
            Long idActual = modoEdicion ? personaSeleccionada.getId() : null;
            if (personaService.existeCorreo(personaSeleccionada.getCorreo(), idActual)) {
                addError("El correo ya esta registrado por otra persona.");
                return;
            }
            personaService.guardar(personaSeleccionada);
            cargarPersonas();
            String msg = modoEdicion ? "Persona actualizada correctamente." : "Persona registrada correctamente.";
            personaSeleccionada = new Persona();
            modoEdicion = false;
            addInfo(msg);
        } catch (Exception e) {
            log.error("Error al guardar persona", e);
            addError("Error al guardar. Intente nuevamente.");
        }
    }

    public void eliminarSeleccionada() {
        try {
            if (personaSeleccionada == null || personaSeleccionada.getId() == null) {
                addError("No hay persona seleccionada para eliminar.");
                return;
            }
            personaService.eliminar(personaSeleccionada.getId());
            cargarPersonas();
            personaSeleccionada = new Persona();
            addInfo("Persona eliminada correctamente.");
        } catch (Exception e) {
            log.error("Error al eliminar persona", e);
            addError("No se pudo eliminar la persona.");
        }
    }

    public void buscar() {
        try {
            log.debug("Buscando con filtro: '{}'", filtro);
            personas = personaService.buscar(filtro);
            log.debug("Resultados: {}", personas.size());
        } catch (Exception e) {
            log.error("Error en busqueda", e);
            addError("Error al realizar la busqueda.");
        }
    }

    public void limpiarFiltro() {
        filtro = "";
        cargarPersonas();
    }

    public void exportarExcel() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Personas");

            // Estilo encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // Fila de encabezados
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"ID", "Nombres", "Apellidos", "Edad", "Correo"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Filas de datos
            List<Persona> datos = (filtro != null && !filtro.trim().isEmpty())
                    ? personas : personaService.listarTodas();

            int rowNum = 1;
            for (Persona p : datos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombres());
                row.createCell(2).setCellValue(p.getApellidos());
                row.createCell(3).setCellValue(p.getEdad());
                row.createCell(4).setCellValue(p.getCorreo());
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Enviar respuesta
            ec.responseReset();
            ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"personas.xlsx\"");

            OutputStream os = ec.getResponseOutputStream();
            workbook.write(os);
            os.flush();

            fc.responseComplete();
            log.debug("Excel exportado con {} registros", rowNum - 1);

        } catch (IOException e) {
            log.error("Error al exportar Excel", e);
            addError("Error al generar el archivo Excel.");
        }
    }

    private void addInfo(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", msg));
    }

    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }

    public List<Persona> getPersonas() { return personas; }
    public void setPersonas(List<Persona> personas) { this.personas = personas; }
    public Persona getPersonaSeleccionada() { return personaSeleccionada; }
    public void setPersonaSeleccionada(Persona p) { this.personaSeleccionada = p; }
    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }
    public boolean isModoEdicion() { return modoEdicion; }
    public void setModoEdicion(boolean modoEdicion) { this.modoEdicion = modoEdicion; }
}