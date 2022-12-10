package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@RestController
@RequestMapping(

)
public class DepartmentController {

    /**
     * This is the variable for the DepartmentDAO in the class
     */
    private DepartmentDAO departmentDAO;
    /**
     * This is the variable for the DepartmentMapper in the class
     */
    private DepartmentMapper departmentMapper;

    /**
     * This is the constructor of the DepartmentController. It set the DepartmentDAO and the DepartmentMapper
     *
     * @param departmentDAO The DAO for department
     * @param departmentMapper The mapper for department
     * @author Tim de Kok
     */
    public DepartmentController(DepartmentDAO departmentDAO, DepartmentMapper departmentMapper) {
        this.departmentDAO = departmentDAO;
        this.departmentMapper = departmentMapper;
    }

    /**
     * This function returns an ApiResponse with a status code and a specific department what will be returned from the departmentDAO
     *
     * @param departmentId The department id what we get from the url
     * @return an ApiResponse with a statuscode and a department
     * @author Tim de Kok
     */
    @RequestMapping(value = ApiConstant.getDepartment, method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<Optional<Department>> getDeparment(@PathVariable String departmentId) {
        final Optional<Department> department = this.departmentDAO.getDepartmentFromDatabase(departmentId);

        if (department.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "Department not found!");
        }

        return new ApiResponseService(HttpStatus.FOUND, department);
    }


    /**
     * This function get all the departments in the database and returns all the departments as a List
     *
     * @return an ApiResponse with a statuscode and a list of all departments
     * @author Tim de Kok
     */
    @RequestMapping(value = ApiConstant.getAllDepartments, method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseService<List<Department>> getAllDepartments() {
        List<Department> allDepartments = this.departmentDAO.getAllDepartmentsFromDatabase();

        return new ApiResponseService(HttpStatus.ACCEPTED, allDepartments);
    }

    /**
     * This function creates an new department in the database and return the specific department back
     *
     * @param departmentDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the department what just got created
     * @author Tim de Kok
     */
    @PostMapping(value = ApiConstant.getAllDepartments, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Department> postDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = this.departmentMapper.toDepartment(departmentDTO);
        this.departmentDAO.postDepartmentToDatabase(department);
        return new ApiResponseService(HttpStatus.CREATED, department);
    }


    /**
     * This function updates an department and returns the department what just got updated back
     *
     * @param departmentId          This is the department id that passed into the url
     * @param departmentDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the department what just got updated
     * @author Tim de Kok
     */
    @PutMapping(value = ApiConstant.getDepartment, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateDepartment(@PathVariable String departmentId, @RequestBody @Valid DepartmentDTO departmentDTO) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.departmentDAO.updateDepartmentInDatabase(departmentId, departmentDTO));
    }


    /**
     * This function removes an department from the database and send an Api response back
     *
     * @param departmentId The department id what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = ApiConstant.getDepartment)
    @ResponseBody
    public ApiResponseService removeDepartment(@PathVariable String departmentId) {
        this.departmentDAO.removeDepartmentFromDatabase(departmentId);

        return new ApiResponseService(HttpStatus.ACCEPTED, "Department has been removed in the database!");
    }
}
