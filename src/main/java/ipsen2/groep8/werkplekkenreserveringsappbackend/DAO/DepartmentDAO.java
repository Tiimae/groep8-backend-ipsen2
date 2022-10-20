package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.DepartmentRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.DepartmentMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DepartmentDAO {

    private DepartmentRepository departmentRepository;
    private DepartmentMapper departmentMapper;

    public DepartmentDAO(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public Optional<Department> getDepartmentFromDatabase(String departmentid) {
        return this.departmentRepository.findById(departmentid);
    }

    public List<Department> getAllDepartmentsFromDatabase() {
        return this.departmentRepository.findAll();
    }

    public void postDepartmentToDatabase(Department department) {
        this.departmentRepository.save(department);
    }

    public void updateDepartmentInDatabase(String id, Department departmentUpdate) {
        Department department = this.departmentRepository.getById(id);
        this.departmentMapper.updateDepartment(department, departmentUpdate);
        this.departmentRepository.save(department);
    }

    public void removeDepartmentFromDatabase(String departmentId) {
        final Department department = this.departmentRepository.findById(departmentId).get();
        for (User user : department.getUsers()) {
            department.getUsers().remove(user);
            user.setDepartment(null);
        }

        this.departmentRepository.deleteById(departmentId);
    }
}
