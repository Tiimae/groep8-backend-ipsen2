package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.WingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.constant.ApiConstant;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.mappers.WingMapper;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import ipsen2.groep8.werkplekkenreserveringsappbackend.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@RestController
@RequestMapping(

)
public class WingController {

    /**
     * This is the variable for the wingDAO in the class
     */
    private final WingDAO wingDAO;
    /**
     * This is the variable for the wingMapper in the class
     */
    private final WingMapper wingMapper;

    /**
     * This is the constructor of the WingController. It set the WingDAO and the WingMapper
     *
     * @param wingDAO    The DAO for wing
     * @param wingMapper The mapper for wing
     * @author Tim de Kok
     */
    public WingController(WingDAO wingDAO, WingMapper wingMapper) {
        this.wingDAO = wingDAO;
        this.wingMapper = wingMapper;
    }

    /**
     * This function returns an ApiResponse with a status code and a specific wing what will be returned from the wingDAO
     *
     * @param wingId The wing wingId what we get from the url
     * @return an ApiResponse with a statuscode and a wing
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getWing)
    @ResponseBody
    public ApiResponseService<Optional<Wing>> getWing(@PathVariable String wingId) {
        Optional<Wing> wing = this.wingDAO.getWingFromDatabase(wingId);

        if (wing.isEmpty()) {
            return new ApiResponseService(HttpStatus.NOT_FOUND, "The wing has not been found!");
        }

        return new ApiResponseService(HttpStatus.FOUND, wing);
    }

    /**
     * This function get all the wings in the database and returns all the wings as a List
     *
     * @return an ApiResponse with a statuscode and a list of all wings
     * @author Tim de Kok
     */
    @GetMapping(value = ApiConstant.getAllWings)
    @ResponseBody
    public ApiResponseService<List<Wing>> getWings() {
        List<Wing> allWings = this.wingDAO.getAllWingsFromDatabase();

        return new ApiResponseService(HttpStatus.ACCEPTED, allWings);
    }

    /**
     * This function creates an new wing in the database and return the specific wing back
     *
     * @param wingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the wing what just got created
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PostMapping(value = ApiConstant.getAllWings, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService<Wing> postWing(@RequestBody @Valid WingDTO wingDTO) throws EntryNotFoundException {
        Wing wing = this.wingMapper.toWing(wingDTO);

        return new ApiResponseService(HttpStatus.CREATED, this.wingDAO.saveWingToDatabase(wing));
    }

    /**
     * This function updates an wing and returns the wing what just got updated back
     *
     * @param wingId      This is the wing wingId that passed into the url
     * @param wingDTO This is the data that was send in the api request
     * @return an ApiResponse with a statuscode and the wing what just got updated
     * @author Tim de Kok
     * @throws EntryNotFoundException because if entry has not been found the program will fail
     */
    @PutMapping(value = ApiConstant.getWing, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService updateWing(@PathVariable String wingId, @RequestBody @Valid WingDTO wingDTO) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.wingDAO.updateWingInDatabase(wingId, wingDTO));
    }

    /**
     * This function removes an wing from the database and send an Api response back
     *
     * @param wingId The wing wingId what we get from the url
     * @return an ApiResponse with a statuscode and message
     * @author Tim de Kok
     */
    @DeleteMapping(value = ApiConstant.getWing)
    @ResponseBody
    public ApiResponseService deleteWing(@PathVariable String wingId) {
        this.wingDAO.deleteWingFromDatabase(wingId);

        return new ApiResponseService(HttpStatus.ACCEPTED, "The wing has been deleted");
    }
}
