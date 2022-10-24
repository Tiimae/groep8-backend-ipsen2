package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.BuildingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.VariableDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Building;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Variable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VariableMapper {

    private BuildingDAO buildingDAO;

    public VariableMapper(BuildingDAO buildingDAO) {
        this.buildingDAO = buildingDAO;
    }

    public Variable toVariable(VariableDTO variableDTO) throws EntryNotFoundException {
        Building building = null;

        if (variableDTO.getBuildingId() != null) {
            final Optional<Building> buildingFromDatabase = this.buildingDAO.getBuildingFromDatabase(variableDTO.getBuildingId());

            if (buildingFromDatabase.isEmpty()) {
                throw new EntryNotFoundException("Building not found!");
            }

            building = buildingFromDatabase.get();
        }

        return new Variable(building);
    }

    public Variable mergeVariable(Variable base, Variable update) {
        base.setBuilding(update.getBuilding());

        return base;
    }
}
