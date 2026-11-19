package recursosrz.Aspirantes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import recursosrz.Repository.aspirantesRepository.aspirantesR;
import recursosrz.Response.aspirantesResponse.Aspirante;
import recursosrz.aspirnatesclaseDos.aspirantesDos;
import recursosrz.exception.validacionException;

public class AspirantesController implements Initializable {

    @FXML
    private TableView<Aspirante> tableAspirantes;
    @FXML
    private TableColumn<Aspirante, String> colId;
    @FXML
    private TableColumn<Aspirante, String> colNombre;
    @FXML
    private TableColumn<Aspirante, String> colApellido;
    @FXML
    private TableColumn<Aspirante, String> colCorreo;
    @FXML
    private TableColumn<Aspirante, String> colTelefono;
    @FXML
    private TableColumn<Aspirante, String> colDireccion;
    @FXML
    private TableColumn<Aspirante, Integer> colEdad;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEdad;

    private final aspirantesR aspiranteRepository = new aspirantesR();
    private String selectedId = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAspirante"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        tableAspirantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedId = newSel.getIdAspirante();
                txtNombre.setText(newSel.getNombre());
                txtApellido.setText(newSel.getApellido());
                txtCorreo.setText(newSel.getCorreo());
                txtTelefono.setText(newSel.getTelefono());
                txtDireccion.setText(newSel.getDireccion());
                txtEdad.setText(String.valueOf(newSel.getEdad()));
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        ObservableList<Aspirante> items = FXCollections.observableArrayList(aspiranteRepository.findAll());
        tableAspirantes.setItems(items);
    }

    @FXML
    public void handleAgregar() {
        try {
            aspirantesDos req = buildRequest();
            Aspirante response = aspiranteRepository.save(req);

            if (response != null) {
                mostrarAlerta("Éxito", "Aspirante Registrado",
                        "Guardado correctamente con ID: " + response.getIdAspirante(), AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (validacionException e) {
            mostrarAlerta("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleActualizar() {
        try {
            if (selectedId == null || selectedId.isBlank()) {
                throw new validacionException("Seleccione un aspirante de la tabla para actualizar.");
            }
            aspirantesDos req = buildRequest();
            req.setIdAspirante(selectedId);

            Aspirante response = aspiranteRepository.update(req);
            if (response != null) {
                mostrarAlerta("Éxito", "Aspirante Actualizado",
                        "Registro actualizado correctamente.", AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (validacionException e) {
            mostrarAlerta("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleEliminar() {
        try {
            if (selectedId == null || selectedId.isBlank()) {
                throw new validacionException("Seleccione un aspirante de la tabla para eliminar.");
            }

            Aspirante response = aspiranteRepository.deleteById(selectedId);
            if (response != null) {
                mostrarAlerta("Éxito", "Aspirante Eliminado",
                        "Se eliminó el registro de " + response.getNombre() + " " + response.getApellido(), AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (validacionException e) {
            mostrarAlerta("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al eliminar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleCerrarSesion() {
        mostrarAlerta("Sesión", "Cerrar sesión", "Conecta aquí tu cambio de escena al login.", AlertType.INFORMATION);
    }

    @FXML
    public void limpiarCampos() {
        selectedId = null;
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEdad.clear();
        tableAspirantes.getSelectionModel().clearSelection();
    }

    private aspirantesDos buildRequest() throws validacionException {
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            aspirantesDos req = new aspirantesDos();
            req.setIdAspirante(selectedId);
            req.setNombre(txtNombre.getText().trim());
            req.setApellido(txtApellido.getText().trim());
            req.setCorreo(txtCorreo.getText().trim());
            req.setTelefono(txtTelefono.getText().trim());
            req.setDireccion(txtDireccion.getText().trim());
            req.setEdad(edad);
            return req;
        } catch (NumberFormatException e) {
            throw new validacionException("La edad debe ser un número entero válido.");
        }
    }

    private void mostrarAlerta(String titulo, String encabezado, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
    

