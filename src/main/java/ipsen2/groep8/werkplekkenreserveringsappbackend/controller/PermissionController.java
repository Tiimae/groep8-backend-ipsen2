package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.PermissionDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.PermissionDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.PermissionMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Permission;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/permission")
public class PermissionController {

    private final PermissionDAO permissionDAO;

    private PermissionMapper permissionMapper;

    public PermissionController(PermissionDAO permissionDAO, PermissionMapper permissionMapper) {
        this.permissionDAO = permissionDAO;
        this.permissionMapper = permissionMapper;
    }

    @RequestMapping(value = "/{permissionid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<Permission> getPermission(@PathVariable String permissionid) {
        Optional<Permission> permission = this.permissionDAO.getPermissionFromDatabase(permissionid);
        if (permission.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The permission has not been found!");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, permission.get());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<List<Permission>> getPermissions() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.permissionDAO.getAllPermissionsFromDatabase());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Permission> postPermission(@RequestBody @Valid PermissionDTO permissionDTO) throws EntryNotFoundException {
        Permission permission = permissionMapper.toPermission(permissionDTO);
        this.permissionDAO.savePermissionToDatabase(permission);
        return new ApiResponseService(HttpStatus.CREATED, permission);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updatePermission(@PathVariable String id, @RequestBody @Valid PermissionDTO permissionDTO) throws EntryNotFoundException {
        Permission updatedPermission = this.permissionMapper.toPermission(permissionDTO);
        final Optional<Permission> permissionFromDatabase = this.permissionDAO.getPermissionFromDatabase(id);
        this.permissionDAO.updatePermissionInDatabase(this.permissionMapper.mergePermission(permissionFromDatabase.get(), updatedPermission));

        return new ApiResponseService(HttpStatus.ACCEPTED, "Permission has been updated");
    }

    @DeleteMapping(value = "/{permissionid}")
    @ResponseBody
    public ApiResponseService deletePermission(@PathVariable String permissionid) {
        this.permissionDAO.deletePermissionFromDatabase(permissionid);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Permission has been deleted");
    }
}