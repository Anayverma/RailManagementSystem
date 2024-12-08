public class SessionManager {
    private static SessionManager instance;
    private String loggedInUsername;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }

    public boolean isLoggedIn() {
        return loggedInUsername != null;
    }
}
