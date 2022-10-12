package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/role")
public class RoleController {

    private final RoleDAO roleDAO;

    public RoleController(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.GET)
    @ResponseBody
    public Role getRole(@PathVariable String roleid) {
        return this.roleDAO.getRoleFromDatabase(roleid);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRole() {
        return this.roleDAO.getAllRolesFromDatabase();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String postRole(@RequestBody Role role) {
        this.roleDAO.saveRoleToDatabase(role);
        return "The role has been posted!";
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public String updateRole(@RequestBody Role role) {
        this.roleDAO.updateRoleToDatabase(role);
        return "The role has been updated!";
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteRole(@PathVariable String roleid) {
        this.roleDAO.removeRoleToDatabase(roleid);
        return "The role has been posted!";
    }

}
