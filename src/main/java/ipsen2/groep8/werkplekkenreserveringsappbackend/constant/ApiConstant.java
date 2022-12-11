package ipsen2.groep8.werkplekkenreserveringsappbackend.constant;

public class ApiConstant {

    public static final String apiPrefix = "/api/";

    //auth routes
    public static final String register = apiPrefix + "auth/register";
    public static final String login = apiPrefix + "auth/login";
    public static final String secret = apiPrefix + "auth/secret";
    public static final String profile = apiPrefix + "auth/profile";
    public static final String toCookie = apiPrefix + "/to-cookie";

    public static final String confirmVerifyToken = apiPrefix + "auth/verify-email/{token}";
    public static final String sendVerifyToken = apiPrefix + "auth/send-verify-email";

    public static final String resetPassword = apiPrefix + "auth/reset-password/{token}";
    public static final String forgotPassword = apiPrefix + "auth/forgot-password/{userEmail}";


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
