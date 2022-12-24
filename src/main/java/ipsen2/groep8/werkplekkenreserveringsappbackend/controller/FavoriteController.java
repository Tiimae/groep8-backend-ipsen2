package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.FavoriteDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.FavoriteDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class FavoriteController {

    private FavoriteDAO favoriteDAO;

    public FavoriteController(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    @GetMapping(value = ApiConstant.getFavoritesByUserId)
    @ResponseBody
    public ApiResponseService getByUserId(@PathVariable String userId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.favoriteDAO.getByUserId(userId));
    }

    @PostMapping(value = ApiConstant.getAllFavorites)
    @ResponseBody
    public ApiResponseService create(@RequestBody FavoriteDTO favoriteDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.CREATED, this.favoriteDAO.create(favoriteDTO));
    }

    @DeleteMapping(value = ApiConstant.getFavorite)
    @ResponseBody
    public ApiResponseService delete(@PathVariable String favoriteId) {
        this.favoriteDAO.remove(favoriteId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Favorite has been removed");
    }
}
