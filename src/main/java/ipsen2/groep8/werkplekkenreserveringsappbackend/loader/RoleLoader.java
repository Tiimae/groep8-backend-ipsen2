package ipsen2.groep8.werkplekkenreserveringsappbackend.loader;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.RoleRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class RoleLoader implements ApplicationRunner {

    private RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        final List<Role> all = this.roleRepository.findAll();

        if (all.size() == 4) {
            return;
        }

        if (all.isEmpty()) {
            this.roleRepository.save(new Role("Admin", new HashSet<>(), new HashSet<>()));
            this.roleRepository.save(new Role("Manager", new HashSet<>(), new HashSet<>()));
            this.roleRepository.save(new Role("Secretary", new HashSet<>(), new HashSet<>()));
            this.roleRepository.save(new Role("User", new HashSet<>(), new HashSet<>()));
        }

    }
}
