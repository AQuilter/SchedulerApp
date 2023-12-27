package sample.Model;

import java.time.ZoneId;

public class Users {

    private int userId;
    private String userName;
    private String userPassword;

   // public ZoneId userTimeZone = ZoneId.systemDefault();

    /** Constructor
     * @param userId the user Id.
     * @param userName the user name.
     * @param userPassword the user password.
     * */
    public Users(int userId, String userName, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    // --- GETTERS ---

    /***
     * @return Returns the user's ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return Returns the user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return Returns the user password.
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @return Returns the user's System default Zone ID.
     */
//    public static ZoneId getUserTimeZone() {
//        return userTimeZone;
//    }
}
