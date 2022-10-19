package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.RoleDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/role")
public class RoleController {

    private final RoleDAO roleDAO;
    private final RoleMapper roleMapper;

    public RoleController(RoleDAO roleDAO, RoleMapper roleMapper) {
        this.roleDAO = roleDAO;
        this.roleMapper = roleMapper;
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Role> getRole(@PathVariable String roleid) {
        Optional<Role> role = this.roleDAO.getRoleFromDatabase(roleid);

        if (role.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "The role has not been found");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, role.get());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<Role>> getRole() {
        return new ApiResponse(HttpStatus.ACCEPTED, this.roleDAO.getAllRolesFromDatabase());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponse postRole(@RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.saveRoleToDatabase(role);
        return new ApiResponse(HttpStatus.CREATED, "The role has been posted!");
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponse updateRole(@PathVariable String id, @RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.updateRoleToDatabase(id, role);

        return new ApiResponse(HttpStatus.ACCEPTED, "The role has been updated!");
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deleteRole(@PathVariable String roleid) {
        this.roleDAO.removeRoleToDatabase(roleid);
        return new ApiResponse(HttpStatus.ACCEPTED, "The role has been posted!");
    }

}
