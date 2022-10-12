package ipsen2.groep8.werkplekkenreserveringsappbackend.controller;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/wing")
public class WingController {
    private final WingDAO wingDAO;

    public WingController(WingDAO wingDAO) {
        this.wingDAO = wingDAO;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Optional<Wing> getWing(@PathVariable String id) {
        return this.wingDAO.getWingFromDatabase(id);
    }

    @GetMapping(value = "")
    @ResponseBody
    public List<Wing> getWings() {
        return this.wingDAO.getAllWingsFromDatabase();
    }

    @PostMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public Wing postWing(@RequestBody Wing wing) {
        this.wingDAO.saveWingToDatabase(wing);
        return wing;
    }

    @PutMapping(value = "", consumes = {"application/json"})
    @ResponseBody
    public Wing updateWing(@RequestBody Wing wing) {
        this.wingDAO.updateWingInDatabase(wing);
        return wing;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public void deleteWing(@PathVariable String id) {
        this.wingDAO.deleteWingFromDatabase(id);
    }
}
