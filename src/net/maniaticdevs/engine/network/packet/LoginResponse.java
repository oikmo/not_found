package net.maniaticdevs.engine.network.packet;

/**
 * Sent to client from server
 * @author Oikmo
 */
public class LoginResponse {
	/** <code>"Login OK"</code> means a good login and NOTHING went wrong */
	private String responseText;
	/** Protocol of server */
	public int PROTOCOL = -1;
	
	/**
	 * Returns the response from server
	 * @return {@link #responseText}
	 */
	public String getResponseText() {
		return responseText;
	}
	
	/**
	 * Sets the response text
	 * @param responseText Sets {@link #responseText}
	 */
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	
}
