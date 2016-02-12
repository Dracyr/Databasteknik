package application;

/**
 * CurrentUser represents the current user that has logged on to
 * the movie booking system. It is a singleton class.
 */
public class CurrentUser {
    /**
     * The single instance of this class
     */
    private static CurrentUser instance;
        
    /**
     * The name of the current user.
     */
    private String currentUserName;
    private int currentUserId;
        
    /**
     * Create a CurrentUser object.
     */
    private CurrentUser() {
        currentUserName = null;
    }
        
    /**
     * Returns the single instance of this class.
     *
     * @return The single instance of the class.
     */
    public static CurrentUser instance() {
        if (instance == null)
            instance = new CurrentUser();
        return instance;
    }
        
    /**
     * Check if a user has logged in.
     *
     * @return true if a user has logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUserName != null;
    }
        
    /** 
     * Get the user id of the current user. Should only be called if
     * a user has logged in.
     *
     * @return The user id of the current user.
     */
    public String getCurrentUserName() {
        return currentUserName == null ? "<none>" : currentUserName;
    }
        
    /**
     * A new user logs in.
     *
     * @param userId The user id of the new user.
     */
    public void loginAs(String username, int userId) {
        currentUserName = username;
        currentUserId = userId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}
