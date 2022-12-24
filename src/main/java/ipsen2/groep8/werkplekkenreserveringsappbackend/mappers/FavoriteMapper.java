package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.FavoriteDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Favorite;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FavoriteMapper {

    private UserDAO userDAO;

    public FavoriteMapper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Favorite toFavorite(FavoriteDTO favoriteDTO) throws EntryNotFoundException {
        User firstUser = null;
        User secondUser = null;
        if (favoriteDTO.getEmail() != null) {
            final Optional<User> userFromDatabaseByEmail = this.userDAO.getUserFromDatabaseByEmail(favoriteDTO.getEmail());

            if (userFromDatabaseByEmail.isEmpty()) {
                throw new EntryNotFoundException("User has not been found!");
            }

            firstUser = userFromDatabaseByEmail.get();
        }

        if (favoriteDTO.getEmail() != null) {
            final Optional<User> userFromDatabase = this.userDAO.getUserFromDatabase(favoriteDTO.getFavoriteId());

            if (userFromDatabase.isEmpty()) {
                throw new EntryNotFoundException("User has not been found!");
            }

            secondUser = userFromDatabase.get();
        }

        return new Favorite(firstUser, secondUser);


    }

}
