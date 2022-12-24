package ipsen2.groep8.werkplekkenreserveringsappbackend.security;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.RoleResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;

    public MyUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepo.findByEmail(email);

        if(userRes.isEmpty()){
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        }

        User user = userRes.get();

        List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();

        for (Role role : user.getRoles()) {
            RoleResolverService roleResolverService = new RoleResolverService();
            String[] roles = roleResolverService.getAuthorizedRoles(role.getName());
            for (String currentRole : roles){
                Stream<GrantedAuthority> foundRole = listAuthorities.stream().filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + currentRole));

                if(foundRole.findAny().isEmpty()){
                    listAuthorities.add(new SimpleGrantedAuthority("ROLE_" + currentRole));
                }
            }

        }

        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                listAuthorities
        );
    }
}