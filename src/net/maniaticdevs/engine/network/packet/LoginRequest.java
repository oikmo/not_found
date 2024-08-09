package net.maniaticdevs.engine.network.packet;

/**
 * Requests login info for Server
 * @author Oikmo
 *
 */
public class LoginRequest {
	/** Name of user */
	private String userName;
	
	/**
	 * Password of user (UNUSED)
	 */
	@Deprecated
	private String userPassword;
	/** Protocol response as to make sure connections aren't gonna be broken */
	public int PROTOCOL = -1;
	
	/**
	 * Returns the name of user
	 * @return {@link #userName}
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the name of user
	 * @param userName Sets {@link #userName}
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Returns the password of user
	 * @return {@link #userPassword}
	 */
	public String getUserPassword() {
		return userPassword;
	}
	
	/**
	 * Sets the password of user
	 * @param userPassword Sets {@link #userPassword}
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
}
