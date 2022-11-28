package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, String> {
    Optional<VerifyToken> findByToken(String token);
}
