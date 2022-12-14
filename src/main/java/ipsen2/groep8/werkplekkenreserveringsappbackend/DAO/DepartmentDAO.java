package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.DepartmentRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DepartmentDAO {

    /**
     * This is the variable for the DepartmentRepository in the class
     */
    private DepartmentRepository departmentRepository;

    /**
     * This is the variable for the DepartmentMapper in the class
     */
    private DepartmentMapper departmentMapper;

    /**
     * This is the constructor of the departmentDAO. It set the departmentMapper and departmentRepository
     *
     * @param departmentRepository The repository for department
     * @param departmentMapper The Mapper for department
     * @author Tim de Kok
     */
    public DepartmentDAO(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Get a specific department out of the database by id and returns that department
     *
     * @param departmentid The id of the department you want to have
     * @return A department out of the database
     * @author Tim de Kok
     */
    public Optional<Department> getDepartmentFromDatabase(String departmentid) {
        return this.departmentRepository.findById(departmentid);
    }

    /**
     * Get all departments out of the database and returns it as a list of departments
     *
     * @return A list of departments out of the database
     * @author Tim de Kok
     */
    public List<Department> getAllDepartmentsFromDatabase() {
        return this.departmentRepository.findAll();
    }

    /**
     * Save a new department to the database
     *
     * @param department The department that needs to be safed in the database
     * @author Tim de Kok
     */
    public void postDepartmentToDatabase(Department department) {
        this.departmentRepository.save(department);
    }

    /**
     * Update a existing department in the database
     *
     * @param id The id of the department what needs to be update
     * @param departmentUpdate The updated version of the department
     * @author Tim de Kok
     */
    public Department updateDepartmentInDatabase(String id, DepartmentDTO departmentUpdate) {
        Department department = this.departmentRepository.getById(id);
        department = this.departmentMapper.updateDepartment(department, departmentUpdate);
        return this.departmentRepository.saveAndFlush(department);
    }

    /**
     * Removes a department out of the database
     *
     * @param departmentId Id of the department what needs to be removed
     * @author Tim de Kok
     */
    public void removeDepartmentFromDatabase(String departmentId) {
        final Department department = this.departmentRepository.findById(departmentId).get();

        for (User user : new ArrayList<User>(department.getUsers())) {
            department.getUsers().remove(user);
            user.setDepartment(null);
        }

        for (Wing wing : new ArrayList<Wing>(department.getWings())) {
            wing.getDepartments().remove(department);
            department.getWings().remove(wing);
        }

        this.departmentRepository.deleteById(departmentId);
    }
}
