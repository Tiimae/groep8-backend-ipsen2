package ipsen2.groep8.werkplekkenreserveringsappbackend.service;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.UserRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.VerifyTokenRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.VerifyToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public int verifyUser(String userId) {
        return userRepository.verifyUser(userId);
    }

    public int resetUser(String userId) {
        return userRepository.resetUser(userId);
    }

    public List<User> all(){
        return userRepository.findAll();
    }
}
