package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;


import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.VariableDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.BuildingDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuildingMapperTest {
    private BuildingMapper buildingMapper;

    @Mock
    private WingDAO wingDAO;
    @Mock
    private VariableDAO variableDAO;

    @Before
    public void setup() {
        this.buildingMapper = new BuildingMapper(this.variableDAO, this.wingDAO);
    }

    @Test
    public void should_returnbuilding_when_tobuildingfunctionhasbeencalled() throws EntryNotFoundException {

        //Arrange
        final Wing wing1 = new Wing("wing1", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
        final Wing wing2 = new Wing("wing2", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
        Set<Wing> wings = new HashSet<>();

        wing1.setId("1");
        wing2.setId("2");

        wings.add(wing1);
        wings.add(wing2);

        final Variable variable = new Variable();
        variable.setId("1");

        Building expectedBuilding = new Building("test", "test", "test", "test", wings, variable);

        final BuildingDTO buildingDTO = new BuildingDTO();
        buildingDTO.setName("test");
        buildingDTO.setAddress("test");
        buildingDTO.setZipcode("test");
        buildingDTO.setCity("test");
        buildingDTO.setWingIds(new String[]{wing1.getId(), wing2.getId()});
        buildingDTO.setVariableId(variable.getId());

        when(this.wingDAO.getWingFromDatabase(wing1.getId())).thenReturn(Optional.of(wing1));
        when(this.wingDAO.getWingFromDatabase(wing2.getId())).thenReturn(Optional.of(wing2));
        when(this.variableDAO.getVariableFromDatabase(variable.getId())).thenReturn(Optional.of(variable));

        //Act
        final Building actualBuilding = this.buildingMapper.toBuilding(buildingDTO);

        //Assert
        assertEquals(expectedBuilding.getName(), actualBuilding.getName());
        assertEquals(expectedBuilding.getAddress(), actualBuilding.getAddress());
        assertEquals(expectedBuilding.getZipcode(), actualBuilding.getZipcode());
        assertEquals(expectedBuilding.getCity(), actualBuilding.getCity());
        assertEquals(expectedBuilding.getWings(), actualBuilding.getWings());
        assertEquals(expectedBuilding.getVariable(), actualBuilding.getVariable());

    }

    @Test
    public void should_returnupdatedbuilding_when_mergebuildingmethodhasbeencalled() {

        //Arrange
        Building oldBuilding = new Building("test", "test", "test", "test", new HashSet<>(), null);
        Building expectedBuilding = new Building("testBuilding", "test", "test", "test", new HashSet<>(), null);


        //Act
        final Building actualBuilding = this.buildingMapper.mergeBuilding(oldBuilding, expectedBuilding);

        //Assert
        assertEquals(expectedBuilding.getName(), actualBuilding.getName());
        assertEquals(expectedBuilding.getAddress(), actualBuilding.getAddress());
        assertEquals(expectedBuilding.getZipcode(), actualBuilding.getZipcode());
        assertEquals(expectedBuilding.getCity(), actualBuilding.getCity());
        assertEquals(expectedBuilding.getWings(), actualBuilding.getWings());
        assertEquals(expectedBuilding.getVariable(), actualBuilding.getVariable());
    }

}
