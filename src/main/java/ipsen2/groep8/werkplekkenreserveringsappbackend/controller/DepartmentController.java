package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/department")
public class DepartmentController {

    private DepartmentDAO departmentDAO;

    public DepartmentController(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
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
    public ApiResponse postDepartment(@RequestBody Department department) {
        this.departmentDAO.postDepartmentToDatabase(department);

        return new ApiResponse(HttpStatus.CREATED, "Department has been posted to the database!");
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public String updateDepartment(@RequestBody Department department) {
        this.departmentDAO.updateDepartmentInDatabase(department);
        return "Department has been updated to the database!";
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse removeDepartment(@PathVariable String departmentId) {
        this.departmentDAO.removeDepartmentFromDatabase(departmentId);

        return new ApiResponse(HttpStatus.ACCEPTED, "Department has been removed in the database!");
    }

    @RequestMapping(value = "/{departmentId}/user/{userId}attach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse addUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        this.departmentDAO.addDepartmentToUserInDatabase(departmentId, userId);

        return new ApiResponse(HttpStatus.ACCEPTED, "the department has been added to the user");
    }

    @RequestMapping(value = "/{departmentId}/user/{userId}/detach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse detachUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        this.departmentDAO.detachDepartmentToUserInDatabase(departmentId, userId);

        return new ApiResponse(HttpStatus.ACCEPTED, "the department has been detached to the user");
    }

}
