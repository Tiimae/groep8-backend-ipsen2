package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/department")
public class DepartmentController {

    private DepartmentDAO departmentDAO;

    public DepartmentController(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @RequestMapping(value = "/{departmentid}", method = RequestMethod.GET)
    @ResponseBody
    public Department getDeparment(@PathVariable String departmentid) {
        return this.departmentDAO.getDepartmentFromDatabase(departmentid);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> getAllDepartments() {
        return this.departmentDAO.getAllDepartmentsFromDatabase();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String postDepartment(@RequestBody Department department) {
        this.departmentDAO.postDepartmentToDatabase(department);
        return "Department has been posted to the database!";
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public String updateDepartment(@RequestBody Department department) {
        this.departmentDAO.updateDepartmentInDatabase(department);
        return "Department has been updated to the database!";
    }

    @RequestMapping(value = "/{departmentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public String removeDepartment(@PathVariable String departmentId) {
        this.departmentDAO.removeDepartmentFromDatabase(departmentId);
        return "Department has been removed in the database!";
    }

    @RequestMapping(value = "/{departmentId}/user/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public String addUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        this.departmentDAO.addDepartmentToUserInDatabase(departmentId, userId);
        return "the department has been added to the user";
    }

    @RequestMapping(value = "/{departmentId}/user/{userId}/detach", method = RequestMethod.POST)
    @ResponseBody
    public String detachUserToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        this.departmentDAO.detachDepartmentToUserInDatabase(departmentId, userId);
        return "the department has been detached to the user";
    }

}
