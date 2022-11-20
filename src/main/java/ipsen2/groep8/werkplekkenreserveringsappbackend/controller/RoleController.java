package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.RoleDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
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
    public ApiResponseService<Role> getRole(@PathVariable String roleid) {
        Optional<Role> role = this.roleDAO.getRoleFromDatabase(roleid);

        if (role.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The role has not been found");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, role.get());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<List<Role>> getRole() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.roleDAO.getAllRolesFromDatabase());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService postRole(@RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.saveRoleToDatabase(role);
        return new ApiResponseService(HttpStatus.CREATED, "The role has been posted!");
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateRole(@PathVariable String id, @RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.updateRoleToDatabase(id, role);

        return new ApiResponseService(HttpStatus.ACCEPTED, "The role has been updated!");
    }

    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponseService deleteRole(@PathVariable String roleid) {
        this.roleDAO.removeRoleToDatabase(roleid);
        return new ApiResponseService(HttpStatus.ACCEPTED, "The role has been posted!");
    }

}
