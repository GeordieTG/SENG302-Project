package nz.ac.canterbury.seng302.tab.formobjects;

/**
 * Creates a form object to be used on the resetPassword page
 */
public class ResetPasswordForm {

    private String password;
    private String confirmPassword;
    private String email;
    private String token;

    public ResetPasswordForm() {
    }

    public ResetPasswordForm(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
