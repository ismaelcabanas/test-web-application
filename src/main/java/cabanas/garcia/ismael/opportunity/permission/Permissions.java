package cabanas.garcia.ismael.opportunity.permission;

import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Permissions {

    private static Map<String, String[]> permissions;
    private static Permissions instance;

    private Permissions(){
        permissions =  new HashMap<>();
    }

    public static Permissions getInstance(){
        if (instance == null){
            instance = new Permissions();
        }
        return instance;
    }

    public void add (String resource, String[] roles){
        assert resource != null;
        permissions.put(resource, roles);

    }

    public Optional<Permission> getPermissions(String resource){
        String[] roles =  permissions.entrySet().stream()
                .filter(map -> resource.equals(map.getValue()))
                .map(map -> map.getValue())
                .collect(Collectors.joining());

        return Optional.of(Permission.builder()
                .resource(resource)
                .roles(roles)
                .build());
    }
}
