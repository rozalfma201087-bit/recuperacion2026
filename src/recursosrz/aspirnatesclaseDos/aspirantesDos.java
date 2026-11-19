package recursosrz.aspirnatesclaseDos;

public class aspirantesDos {

 /* ===============================
            ATRIBUTOS
        =============================== */
    private String idAspirante;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private int edad;
    private int idDireccion;

    /* ===============================
            CONSTRUCTOR
        =============================== */
    public aspirantesDos() {
    }

    public aspirantesDos(String idAspirante, String nombre, String apellido, String correo, String telefono, int edad, int idDireccion) {
        this.idAspirante = idAspirante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.edad = edad;
        this.idDireccion = idDireccion;
    }

    /* ===============================
            GETTERS&SETTERS
        =============================== */
    public String getIdAspirante() {
        return idAspirante;
    }

    public void setIdAspirante(String idAspirante) {
        this.idAspirante = idAspirante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

private String direccion;

public String getDireccion() {
    return direccion;
}

public void setDireccion(String direccion) {
    this.direccion = direccion;
}

}
    

