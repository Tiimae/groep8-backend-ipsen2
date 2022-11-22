package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.RoleDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.RoleMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Controller
@RequestMapping("/api/role")
public class RoleController {

    /**
     * This is the variable for the RoleDAO in the class
     */
    private final RoleDAO roleDAO;

    /**
     * This is the variable for the RoleMapper in the class
     */
    private final RoleMapper roleMapper;

    /**
     * This is the constructor of the RoleController. It set the roleDAO and the roleMapper
     *
     * @param roleDAO The DAO for role
     * @param roleMapper   The mapper for role
     * @author Tim de Kok
     */
    public RoleController(RoleDAO roleDAO, RoleMapper roleMapper) {
        this.roleDAO = roleDAO;
        this.roleMapper = roleMapper;
    }

    /**
     * This function returns an ApiResponse with a status code and a specific role what will be returned from the roleDAO
     *
     * @param roleid The role id what we get from the url
     * @return an ApiResponse with a statuscode and a role
     * @author Tim de Kok
     */
    @RequestMapping(value = "/{roleid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<Role> getRole(@PathVariable String roleid) {
        Optional<Role> role = this.roleDAO.getRoleFromDatabase(roleid);

        if (role.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The role has not been found");
        }

        return new ApiResponseService(HttpStatus.FOUND, role.get());
    }

    /**
     * This function get all the roles in the database and returns all the roles as a List
     *
     * @return an ApiResponse with a statuscode and a list of all roles
     * @author Tim de Kok
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<List<Role>> getRole() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.roleDAO.getAllRolesFromDatabase());
    }

    /**
     * This function creates an new role in the database and return the specific role back
     *
     * @param roleDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the role what just got created
     * @author Tim de Kok
     */
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Role> postRole(@RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.saveRoleToDatabase(role);
        return new ApiResponseService(HttpStatus.CREATED, role);
    }

    /**
     * This function updates an role and returns the role what just got updated back
     *
     * @param id      This is the role id that passed into the url
     * @param roleDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the role what just got updated
     * @author Tim de Kok
     */
    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateRole(@PathVariable String id, @RequestBody @Valid RoleDTO roleDTO) {
        Role role = this.roleMapper.toRole(roleDTO);
        this.roleDAO.updateRoleToDatabase(id, role);

        return new ApiResponseService(HttpStatus.ACCEPTED, "Role has been updated!");
    }

    /**
     * This function removes an role from the database and send an Api response back
     *
     * @param roleid The role id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @RequestMapping(value = "/{roleid}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponseService deleteRole(@PathVariable String roleid) {
        this.roleDAO.removeRoleToDatabase(roleid);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Role has been deleted!");
    }

}
