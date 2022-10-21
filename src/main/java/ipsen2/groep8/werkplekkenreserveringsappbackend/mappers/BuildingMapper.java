package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.VariableDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Variable;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BuildingMapper {

    private VariableDAO variableDAO;
    private WingDAO wingDAO;

    public BuildingMapper(VariableDAO variableDAO, WingDAO wingDAO) {
        this.variableDAO = variableDAO;
        this.wingDAO = wingDAO;
    }

    public Building toBuilding(BuildingDTO buildingDTO) throws EntryNotFoundException {
        String name = buildingDTO.getName();
        String address = buildingDTO.getAddress();
        String zipcode = buildingDTO.getZipcode();
        String city = buildingDTO.getCity();
        Set<Wing> wings = new HashSet<>();
        Variable variable = null;

        wings = Arrays.stream(buildingDTO.getWingIds())
                .map(id -> this.wingDAO.getWingFromDatabase(id).orElse(null))
                .collect(Collectors.toSet());

        if (buildingDTO.getVariableId() != null) {
            final Optional<Variable> variableFromDatabase = this.variableDAO.getVariableFromDatabase(buildingDTO.getVariableId());

            if (variableFromDatabase.isEmpty()) {
                throw new EntryNotFoundException("Variable not found");
            }

            variable = variableFromDatabase.get();
        }

        return new Building(name, address, zipcode, city, wings, variable);
    }

    public Building mergeBuilding(Building base, Building update) {
        base.setName(update.getName());
        base.setAddress(update.getAddress());
        base.setZipcode(update.getZipcode());
        base.setCity(update.getCity());
        base.setWings(update.getWings());
        base.setVariable(update.getVariable());

        return base;
    }
}
