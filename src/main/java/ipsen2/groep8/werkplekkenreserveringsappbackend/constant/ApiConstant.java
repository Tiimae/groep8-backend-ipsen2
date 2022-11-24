package ipsen2.groep8.werkplekkenreserveringsappbackend.constant;

public class ApiConstant {

    public static final String apiPrefix = "/api/";

    //auth routes
    public static final String register = apiPrefix + "auth/register";
    public static final String login = apiPrefix + "auth/login";

    // Building routes
    public static final String getBuilding = apiPrefix + "building/{buildingId}";
    public static final String getAllBuildings = apiPrefix + "building";

    // Department routes
    public static final String getDepartment = apiPrefix + "department/{departmentId}";
    public static final String getAllDepartments = apiPrefix + "department";

    // Meeting room routes
    public static final String getMeetingRoom = apiPrefix + "meeting-room/{meetingRoomId}";
    public static final String getAllMeetingRooms = apiPrefix + "meeting-room";

    // Department routes
    public static final String getReservation = apiPrefix + "reservation/{reservationId}";
    public static final String getReservationUser = apiPrefix + "reservation/{reservationId}/user";
    public static final String getAllReservations = apiPrefix + "reservation";

    // Department routes
    public static final String getRole = apiPrefix + "role/{roleId}";
    public static final String getAllRoles = apiPrefix + "role";

    // Department routes
    public static final String getUser = apiPrefix + "user/{userId}";
    public static final String getUserReservations = apiPrefix + "user/{userId}/reservation";
    public static final String getAllUsers = apiPrefix + "user";

    // Department routes
    public static final String getWing = apiPrefix + "wing/{wingId}";
    public static final String getAllWings = apiPrefix + "wing";
}