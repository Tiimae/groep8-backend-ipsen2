package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository;

import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {
    List<Favorite> getFavoritesByUserId(String userId);
}
