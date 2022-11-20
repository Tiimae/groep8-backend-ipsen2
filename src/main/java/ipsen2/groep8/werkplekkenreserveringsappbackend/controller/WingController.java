package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.WingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/wing")
public class WingController {

    private final WingDAO wingDAO;
    private final WingMapper wingMapper;

    public WingController(WingDAO wingDAO, WingMapper wingMapper) {
        this.wingDAO = wingDAO;
        this.wingMapper = wingMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ApiResponseService<Optional<Wing>> getWing(@PathVariable String id) {
        Optional<Wing> wing = this.wingDAO.getWingFromDatabase(id);

        if (wing.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The wing has not been found!");
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, wing);
    }

    @GetMapping(value = "")
    @ResponseBody
    public ApiResponseService<List<Wing>> getWings() {
        List<Wing> allWings = this.wingDAO.getAllWingsFromDatabase();

        return new ApiResponseService(HttpStatus.ACCEPTED, allWings);
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService postWing(@RequestBody @Valid WingDTO wingDTO) throws EntryNotFoundException {

        this.wingDAO.saveWingToDatabase(this.wingMapper.toWing(wingDTO));

        return new ApiResponseService(HttpStatus.CREATED, "The wing has been posted!");
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateWing(@PathVariable String id, @RequestBody @Valid WingDTO wingDTO) throws EntryNotFoundException {
        this.wingDAO.updateWingInDatabase(id, this.wingMapper.toWing(wingDTO));
        return new ApiResponseService(HttpStatus.ACCEPTED, "The wing has been updated!");
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ApiResponseService deleteWing(@PathVariable String id) {
        this.wingDAO.deleteWingFromDatabase(id);

        return new ApiResponseService(HttpStatus.ACCEPTED, "The wing has been deleted");
    }
}
