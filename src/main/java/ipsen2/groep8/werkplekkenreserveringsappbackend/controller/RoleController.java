package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<Optional<Role>> getRole(@PathVariable String roleid) {
        Optional<Role> role = this.roleDAO.getRoleFromDatabase(roleid);

        if (role.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "The role has not been found");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, role);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<Role>> getRole() {
        List<Role> allRoles = this.roleDAO.getAllRolesFromDatabase();

        return new ApiResponse(HttpStatus.ACCEPTED, allRoles);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse postRole(@RequestBody Role role) {
        this.roleDAO.saveRoleToDatabase(role);
        return new ApiResponse(HttpStatus.CREATED, "The role has been posted!");
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse updateRole(@RequestBody Role role) {
        this.roleDAO.updateRoleToDatabase(role);
        return new ApiResponse(HttpStatus.ACCEPTED, "The role has been updated!");
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deleteRole(@PathVariable String roleid) {
        this.roleDAO.removeRoleToDatabase(roleid);
        return new ApiResponse(HttpStatus.ACCEPTED, "The role has been posted!");
    }

}
