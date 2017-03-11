package cabanas.garcia.ismael.opportunity.model;

public enum RoleEnum {
    ADMIN("Admin");

    private final String rolename;

    RoleEnum(String rolename) {
        this.rolename = rolename;
    }

    public String getRoleName() {
        return rolename;
    }
}
