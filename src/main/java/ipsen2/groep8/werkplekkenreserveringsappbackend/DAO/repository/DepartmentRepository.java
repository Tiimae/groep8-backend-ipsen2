package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
}
