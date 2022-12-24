package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import java.util.HashMap;

public class RoleResolverService {
    private final HashMap<String, String[]> inheritRoles;

    public RoleResolverService(){
        this.inheritRoles = new HashMap<>();
        this.inheritRoles.put("User", new String[]{"User"});
        this.inheritRoles.put("Manager", new String[]{"Manager", "User"});
        this.inheritRoles.put("Secretary", new String[]{"Secretary", "User"});
        this.inheritRoles.put("Admin", new String[]{"Admin", "Manager","Secretary", "User"});
    }

    public String[] getAuthorizedRoles(String roleName){
        return this.inheritRoles.get(roleName);
    }
}
