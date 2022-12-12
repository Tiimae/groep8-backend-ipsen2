package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.verified = TRUE WHERE u.id = ?1")
    int verifyUser(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.reset_required = FALSE WHERE u.id = ?1")
    int resetUser(String userId);

}
