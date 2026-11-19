package recursosrz.authRequest;

public class loginRequest {

private final String username;
    private final String password;

    public loginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
