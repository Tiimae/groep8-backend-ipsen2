package ipsen2.groep8.werkplekkenreserveringsappbackend.DAO;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.repository.FavoriteRepository;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.FavoriteDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.FavoriteMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Favorite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoriteDAO {

    private FavoriteRepository favoriteRepository;

    private FavoriteMapper favoriteMapper;

    public FavoriteDAO(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
    }

    public List<Favorite> getByUserId(String userId) {
        return this.favoriteRepository.getFavoritesByUserId(userId);
    }

    public Favorite create(FavoriteDTO favoriteDTO) throws EntryNotFoundException {
        return this.favoriteRepository.save(this.favoriteMapper.toFavorite(favoriteDTO));
    }
}
