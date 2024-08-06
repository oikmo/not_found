package net.maniaticdevs.engine.network.packet;

public class LoginResponse {

	private String responseText;
	public int PROTOCOL = -1;
	
	public String getResponseText(){
		return responseText;
	}
	
	public void setResponseText(String responseText){
		this.responseText = responseText;
	}
	
}
