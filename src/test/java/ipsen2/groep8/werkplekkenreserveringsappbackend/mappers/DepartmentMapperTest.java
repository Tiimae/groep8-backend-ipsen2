package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.UserDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.WingDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.DepartmentDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Wing;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentMapperTest {
    private DepartmentMapper departmentMapper;

    @Mock private WingDAO wingDAO;
    @Mock private UserDAO userDAO;

    @Before
    public void setup() {
        this.departmentMapper = new DepartmentMapper(wingDAO, userDAO);
    }

    @Test
    public void should_returndepartment_when_todeparmentfunctionhasbeencalled() throws EntryNotFoundException {

        //Arrange
        final User user1 = new User("test1", "test1", "test1", new HashSet<>(), null, new HashSet<>());
        final User user2 = new User("test2", "test2", "test2", new HashSet<>(), null, new HashSet<>());
        Set<User> users = new HashSet<>();

        user1.setId("1");
        user2.setId("2");

        users.add(user1);
        users.add(user2);

        final Wing wing1 = new Wing("test1", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
        final Wing wing2 = new Wing("test1", null, null, new HashSet<>(), null, new HashSet<>(), new HashSet<>());
        Set<Wing> wings = new HashSet<>();

        wing1.setId("1");
        wing2.setId("2");

        wings.add(wing1);
        wings.add(wing2);

        final DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("test");
        departmentDTO.setUserIds(new String[] {user1.getId(), user2.getId()});
        departmentDTO.setWingIds(new String[] {wing1.getId(), wing2.getId()});

        final Department expectedDepartment = new Department("test", users, wings);

        when(this.userDAO.getUserFromDatabase(user1.getId())).thenReturn(Optional.of(user1));
        when(this.userDAO.getUserFromDatabase(user2.getId())).thenReturn(Optional.of(user2));
        when(this.wingDAO.getWingFromDatabase(wing1.getId())).thenReturn(Optional.of(wing1));
        when(this.wingDAO.getWingFromDatabase(wing2.getId())).thenReturn(Optional.of(wing2));

        //Act
        final Department actualDepartment = this.departmentMapper.toDepartment(departmentDTO);

        //Assign
        assertEquals(expectedDepartment.getName(), actualDepartment.getName());
        assertEquals(expectedDepartment.getUsers(), actualDepartment.getUsers());
        assertEquals(expectedDepartment.getWings(), actualDepartment.getWings());
    }

    @Test
    public void should_returnupdateddepartment_when_mergedepartemntmethodhasbeencalled() {

        //Assert
        final Department department = new Department("test", new HashSet<>(), new HashSet<>());
        final Department expectedDepartment = new Department("test", new HashSet<>(), new HashSet<>());

        //Act

        final Department actualDepartment = this.departmentMapper.updateDepartment(department, expectedDepartment);

        //Assign
        assertEquals(expectedDepartment.getName(), actualDepartment.getName());
    }
}
