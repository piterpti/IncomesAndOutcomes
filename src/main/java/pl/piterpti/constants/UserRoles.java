package pl.piterpti.constants;

/**
 * Constants for user roles
 * 
 * @author Piter
 *
 */
public class UserRoles {
	
	public static final String USER_ROLE_ADMIN = "ADMIN";
	public static final String USER_ROLE_USER = "USER";
	
	private static String[] ROLES = {
		USER_ROLE_ADMIN,
		USER_ROLE_USER
	};
	
	/**
	 * 
	 * @return all available roles
	 */
	public static String[] getRoles() {
		return ROLES;
	}
}
