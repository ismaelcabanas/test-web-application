package cabanas.garcia.ismael.opportunity.steps.model;

public class UserData {
    private String username;
    private String password;
    private String roles;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String[] getRoles() {
        return roles.split(",");
    }
}
