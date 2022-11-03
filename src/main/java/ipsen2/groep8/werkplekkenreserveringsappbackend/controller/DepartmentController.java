package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/department")
public class DepartmentController {

    private DepartmentDAO departmentDAO;
    private DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentDAO departmentDAO, DepartmentMapper departmentMapper) {
        this.departmentDAO = departmentDAO;
        this.departmentMapper = departmentMapper;
    }

    @RequestMapping(value = "/{departmentid}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Optional<Department>> getDeparment(@PathVariable String departmentid) {
        final Optional<Department> department = this.departmentDAO.getDepartmentFromDatabase(departmentid);

        if (department.isEmpty()) {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Department not found!");
        }

        return new ApiResponse(HttpStatus.ACCEPTED, department);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<List<Department>> getAllDepartments() {
        List<Department> allDepartments = this.departmentDAO.getAllDepartmentsFromDatabase();

        return new ApiResponse(HttpStatus.ACCEPTED, allDepartments);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse postDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = this.departmentMapper.toDepartment(departmentDTO);
        this.departmentDAO.postDepartmentToDatabase(department);
        return new ApiResponse(HttpStatus.CREATED, department);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse updateDepartment(@PathVariable String id, @RequestBody @Valid DepartmentDTO departmentDTO) {
        Department department = this.departmentMapper.toDepartment(departmentDTO);
        this.departmentDAO.updateDepartmentInDatabase(id, department);
        return new ApiResponse(HttpStatus.ACCEPTED, "Department has been added");
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse removeDepartment(@PathVariable String departmentId) {
        this.departmentDAO.removeDepartmentFromDatabase(departmentId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Department has been removed in the database!");
    }
//
//    @RequestMapping(value = "/{departmentId}/user/{userId}attach", method = RequestMethod.POST)
//    @ResponseBody
//    public ApiResponse addUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
//        this.departmentDAO.addDepartmentToUserInDatabase(departmentId, userId);
//
//        return new ApiResponse(HttpStatus.ACCEPTED, "the department has been added to the user");
//    }
//
//    @RequestMapping(value = "/{departmentId}/user/{userId}/detach", method = RequestMethod.POST)
//    @ResponseBody
//    public ApiResponse detachUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
//        this.departmentDAO.detachDepartmentToUserInDatabase(departmentId, userId);
//
//        return new ApiResponse(HttpStatus.ACCEPTED, "the department has been detached to the user");
//    }

}
