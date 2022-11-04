package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tim de Kok
 * @version 1.0
 */
@Component
public class BuildingMapper {

    /**
     * This is the variable for the WingDAO in the class
     */
    private WingDAO wingDAO;

    /**
     * This is the consturctor of the BuildingMapper. It set the WingDAO
     *
     * @param wingDAO The DAO for wing
     * @author Tim de Kok
     */
    public BuildingMapper(WingDAO wingDAO) {
        this.wingDAO = wingDAO;
    }

    /**
     * This function will be used to create a new building in the database
     *
     * @param buildingDTO The building data to create a new Building
     * @return a new Building
     * @author Tim de Kok
     */
    public Building toBuilding(BuildingDTO buildingDTO) throws EntryNotFoundException {
        String name = buildingDTO.getName();
        String address = buildingDTO.getAddress();
        String zipcode = buildingDTO.getZipcode();
        String city = buildingDTO.getCity();
        Set<Wing> wings = new HashSet<>();

        wings = Arrays.stream(buildingDTO.getWingIds())
                .map(id -> this.wingDAO.getWingFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        return new Building(name, address, zipcode, city, wings);
    }

    /**
     * This function returns an update building, so we can save it in the database
     *
     * @param base The building data that already exist in the database
     * @param update The building data to create a new Building
     * @return an updated building
     * @author Tim de Kok
     */
    public Building mergeBuilding(Building base, Building update) {
        base.setName(update.getName());
        base.setAddress(update.getAddress());
        base.setZipcode(update.getZipcode());
        base.setCity(update.getCity());
        base.setWings(update.getWings());

        return base;
    }
}
