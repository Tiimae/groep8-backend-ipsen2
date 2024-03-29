package ipsen2.groep8.werkplekkenreserveringsappbackend.mappers;

import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.DepartmentDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.ReservationDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DAO.RoleDAO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.DTO.UserDTO;
import ipsen2.groep8.werkplekkenreserveringsappbackend.exceptions.EntryNotFoundException;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Reservation;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Role;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.User;
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
public class UserMapperTest {

    private UserMapper userMapper;

    @Mock private DepartmentDAO departmentDAO;
    @Mock private RoleDAO roleDAO;
    @Mock private ReservationDAO reservationDAO;

    @Before
    public void setup() {
        this.userMapper = new UserMapper(this.departmentDAO, this.roleDAO, this.reservationDAO);
    }

    @Test
    public void should_returnauser_when_touserfunctionhasbeencalled() throws EntryNotFoundException {
        //Arrange
        Reservation reservation1 = new Reservation(null, null, false, 0, "", null, new HashSet<>(), null, null);
        Reservation reservation2 = new Reservation(null, null, false, 0, "", null, new HashSet<>(), null, null);
        Reservation reservation3 = new Reservation(null, null, false, 0, "", null, new HashSet<>(), null, null);
        Set<Reservation> reservations = new HashSet<>();

        reservation1.setId("1");
        reservation2.setId("2");
        reservation3.setId("3");

        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);

        Role role1 = new Role("role1", new HashSet<>());
        Role role2 = new Role("role2",  new HashSet<>());
        Role role3 = new Role("role3",  new HashSet<>());
        Set<Role> roles = new HashSet<>();

        role1.setId("1");
        role2.setId("2");
        role3.setId("3");

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        Department department = new Department("department1", new HashSet<>(), new HashSet<>());
        department.setId("1");

        when(this.reservationDAO.getReservationFromDatabase(reservation1.getId())).thenReturn(Optional.of(reservation1));
        when(this.reservationDAO.getReservationFromDatabase(reservation2.getId())).thenReturn(Optional.of(reservation2));
        when(this.reservationDAO.getReservationFromDatabase(reservation3.getId())).thenReturn(Optional.of(reservation3));
        when(this.roleDAO.getRoleFromDatabase(role1.getId())).thenReturn(Optional.of(role1));
        when(this.roleDAO.getRoleFromDatabase(role2.getId())).thenReturn(Optional.of(role2));
        when(this.roleDAO.getRoleFromDatabase(role3.getId())).thenReturn(Optional.of(role3));
        when(this.departmentDAO.getDepartmentFromDatabase(department.getId())).thenReturn(Optional.of(department));

        final UserDTO userDTO = new UserDTO();
        userDTO.setName("test");
        userDTO.setEmail("test");
        userDTO.setPassword("test");
        userDTO.setVerified(true);
        userDTO.setResetRequired(true);
        userDTO.setDepartmentId(department.getId());
        userDTO.setReservationIds(new String[]{reservation1.getId(), reservation2.getId(), reservation3.getId()});
        userDTO.setRoleIds(new String[]{role1.getId(), role2.getId(), role3.getId()});

        final User expectedUser = new User("test", "test", "test", true, true, roles, department, reservations);

        //Act
        final User actualUser = this.userMapper.toUser(userDTO);

        //Assert
        assertEquals(actualUser.getName(), expectedUser.getName());
        assertEquals(actualUser.getPassword(), expectedUser.getPassword());
        assertEquals(actualUser.getEmail(), expectedUser.getEmail());
        assertEquals(actualUser.getDepartment(), expectedUser.getDepartment());
        assertEquals(actualUser.getReservations(), expectedUser.getReservations());
        assertEquals(actualUser.getRoles(), expectedUser.getRoles());
    }

    @Test
    public void should_returnupdateduser_when_mergeusermethodhasbeencalled() throws EntryNotFoundException {

        //Arrange
        final User oldUser = new User("test", "test", "test", true, true,  new HashSet<>(), new Department(), new HashSet<>());

        final UserDTO userDTO = new UserDTO();
        userDTO.setName("Tim");
        userDTO.setEmail("Test@Test.nl");
        userDTO.setPassword("test");
        userDTO.setDepartmentId(null);
        userDTO.setVerified(false);
        userDTO.setReservationIds(new String[0]);
        userDTO.setRoleIds(new String[0]);

        final User expectedUser = this.userMapper.toUser(userDTO);


        //Act
        final User actualUser = this.userMapper.mergeUser(oldUser, userDTO);

        //Assert
        assertEquals(actualUser.getName(), expectedUser.getName());
        assertEquals(actualUser.getPassword(), expectedUser.getPassword());
        assertEquals(actualUser.getEmail(), expectedUser.getEmail());
        assertEquals(actualUser.getDepartment(), expectedUser.getDepartment());
        assertEquals(actualUser.getReservations(), expectedUser.getReservations());
        assertEquals(actualUser.getRoles(), expectedUser.getRoles());
    }

}
