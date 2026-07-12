package lk.sampath.casadminportalms.dto.userSession;

public class UserContext {
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> SOL_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    private static final ThreadLocal<String> SECURITY_CLASS = new ThreadLocal<>();

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void setSolId(String solId) {
        SOL_ID.set(solId);
    }

    public static String getSolId() {
        return SOL_ID.get();
    }

    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static void setSecurityClass(String securityClass) {
        SECURITY_CLASS.set(securityClass);
    }

    public static String getSecurityClass() {
        return SECURITY_CLASS.get();
    }

    public static void clear() {
        USER_ID.remove();
        SOL_ID.remove();
        USERNAME.remove();
        SECURITY_CLASS.remove();
    }
}
