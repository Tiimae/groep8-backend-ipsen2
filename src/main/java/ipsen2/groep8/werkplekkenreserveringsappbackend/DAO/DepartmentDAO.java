package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.DepartmentRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.WingRepository;
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
    private UserRepository userRepository;
    private WingRepository wingRepository;

    public DepartmentDAO(DepartmentRepository departmentRepository, UserRepository userRepository, WingRepository wingRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.wingRepository = wingRepository;
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

    public void updateDepartmentInDatabase(Department department) {
        this.departmentRepository.save(department);
    }

    public void removeDepartmentFromDatabase(String departmentId) {
        final Department department = this.departmentRepository.findById(departmentId).get();
        for (User user : department.getUsers()) {
            department.getUsers().remove(user);
            user.setDepartment(null);
            this.userRepository.save(user);
        }

        this.departmentRepository.deleteById(departmentId);
    }

    public void addDepartmentToUserInDatabase(String departmentid, String userid) {
        final User user = this.userRepository.findById(userid).get();
        final Department department = this.departmentRepository.findById(departmentid).get();

        user.setDepartment(department);
        this.userRepository.save(user);
    }

    public void detachDepartmentToUserInDatabase(String departmentid, String userid) {
        final Department department = this.departmentRepository.findById(departmentid).get();
        final User user = this.userRepository.findById(userid).get();

        department.getUsers().remove(user);
        user.setDepartment(null);
        this.userRepository.save(user);
    }


    public void attachWingToDepartmentInDatabase(String departmentId, String wingId) {
        final Wing wing = this.wingRepository.findById(wingId).get();
        final Department department = this.departmentRepository.findById(departmentId).get();

        final Set<Wing> wings = department.getWings();
        wings.add(wing);
        department.setWings(wings);

        this.departmentRepository.save(department);
    }

    public void detachWingFromDepartmentInDatabase(String departmentId, String wingId) {
        final Wing wing = this.wingRepository.findById(wingId).get();
        final Department department = this.departmentRepository.findById(departmentId).get();

        department.getWings().remove(wing);
        this.departmentRepository.save(department);
    }
}
