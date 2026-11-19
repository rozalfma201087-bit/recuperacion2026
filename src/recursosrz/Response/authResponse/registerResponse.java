package recursosrz.Response.authResponse;

public class registerResponse {

   private String idUsuario;
    private String message;
    private boolean exito;

    public registerResponse(String idUsuario, String message, boolean exito) {
        this.idUsuario = idUsuario;
        this.message = message;
        this.exito = exito;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}    
