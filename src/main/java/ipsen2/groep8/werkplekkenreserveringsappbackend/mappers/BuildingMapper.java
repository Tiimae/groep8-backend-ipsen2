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

@Component
public class BuildingMapper {

    private WingDAO wingDAO;

    public BuildingMapper(WingDAO wingDAO) {
        this.wingDAO = wingDAO;
    }

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

    public Building mergeBuilding(Building base, Building update) {
        base.setName(update.getName());
        base.setAddress(update.getAddress());
        base.setZipcode(update.getZipcode());
        base.setCity(update.getCity());
        base.setWings(update.getWings());

        return base;
    }
}
