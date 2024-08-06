package net.maniaticdevs.engine.network.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginResponse;
import net.maniaticdevs.engine.network.packet.PacketAddObject;
import net.maniaticdevs.engine.network.packet.PacketAddPlayer;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.network.packet.PacketGameJoin;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.network.packet.PacketRemovePlayer;
import net.maniaticdevs.engine.network.packet.PacketUpdateAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateDirection;
import net.maniaticdevs.engine.network.packet.PacketUpdateX;
import net.maniaticdevs.engine.network.packet.PacketUpdateY;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.level.SampleLevel;

public class PlayerClientListener extends Listener {
	
	public static List<OBJ> specialOBJs = new ArrayList<>();
	
	public void received(Connection connection, Object object){
		//System.out.println(object);
		
		if(!Thread.currentThread().getName().contentEquals("PlayerClientListener Thread")) {
			Thread.currentThread().setName("PlayerClientListener Thread");
		}

		if(object instanceof LoginResponse){
			LoginResponse response = (LoginResponse) object;
			if(response.getResponseText().equalsIgnoreCase("ok")){
				Logger.log(LogLevel.INFO,"Login Ok");
			} else {
				Main.theNetwork.disconnect();
				System.out.println(response.PROTOCOL + " " + NetworkHandler.NETWORK_PROTOCOL);
				if(response.PROTOCOL != NetworkHandler.NETWORK_PROTOCOL) {
					Main.disconnect(false, Main.lang.translateKey("network.disconnect.p").replace("%s", ""+response.PROTOCOL));
				} else {
					Main.disconnect(false, Main.lang.translateKey("network.disconnect.l"));
				}

				Logger.log(LogLevel.WARN,"Login failed");
			}
		}

		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;

			OtherPlayer newPlayer = new OtherPlayer();
			if(Main.theNetwork == null) {
				return;
			} else {
				if(!Main.theNetwork.players.containsKey(packet.id)) {
					Main.theNetwork.players.put(packet.id, newPlayer);
				}
			}
		}
		else if(object instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) object;
			if(Main.theNetwork == null) {
				return;
			}
			
			if(Main.theNetwork.client == null) {
				return;
			}
			if(packet.id == Main.theNetwork.client.getID()) {
				Main.theNetwork.disconnect();
				Main.disconnect(packet.kick, packet.message);
			} else {
				if(Main.thePlayer != null) {
					if(Main.theNetwork.players.get(packet.id) != null && Main.theNetwork.players.get(packet.id).userName != null) {
						if(!Main.theNetwork.players.get(packet.id).userName.contentEquals(Main.theNetwork.player.userName)) {
							/*Main.theNetwork.rawMessages.add(new ChatMessage(Main.theNetwork.players.get(packet.id).userName + " left the game", true));
							if(Main.currentScreen instanceof GuiChat) {
								((GuiChat)Main.currentScreen).updateMessages();
							}*/
						}

					}
				}
				Main.theNetwork.players.remove(packet.id);

			}
		}
		else if(object instanceof PacketUserName){
			PacketUserName packet = (PacketUserName) object;
			if(Main.theNetwork == null) {
				Main.theNetwork.disconnect();
				Main.disconnect(false, Main.lang.translateKey("network.disconnect.u"));
			} else if(Main.theNetwork.players == null) {
				Main.theNetwork.players = new HashMap<>();
			}

			if(!Main.theNetwork.players.containsKey(packet.id) && packet.id != Main.theNetwork.client.getID()) {
				Main.theNetwork.players.put(packet.id, new OtherPlayer());
				OtherPlayer p = Main.theNetwork.players.get(packet.id);
				p.userName = packet.userName;
				p.id = connection.getID();
				
				if(Main.thePlayer != null) {
					if(packet.userName != null) {
						if(!Main.theNetwork.players.get(packet.id).userName.contentEquals(Main.theNetwork.player.userName)) {
							//Main.theNetwork.rawMessages.add(new ChatMessage(packet.userName + " joined the game", true));

							/*if(Main.currentScreen instanceof GuiChat) {
								((GuiChat)Main.currentScreen).updateMessages();
							}*/
							if(Main.theNetwork.players.get(packet.id).userName == null) {
								Main.theNetwork.players.get(packet.id).userName =  packet.userName;
							}
						}

					}
				}
			} else if(Main.theNetwork.players.containsKey(packet.id)) {
				OtherPlayer p = Main.theNetwork.players.get(packet.id);
				p.userName = packet.userName;
				
				if(Main.thePlayer != null) {
					if(packet.userName != null) {
						if(!Main.theNetwork.players.get(packet.id).userName.contentEquals(Main.theNetwork.player.userName)) {
							/*Main.theNetwork.rawMessages.add(new ChatMessage(packet.userName + " joined the game", true));
							if(Main.currentScreen instanceof GuiChat) {
								((GuiChat)Main.currentScreen).updateMessages();
							}*/
							if(Main.theNetwork.players.get(packet.id).userName == null) {
								Main.theNetwork.players.get(packet.id).userName =  packet.userName;	
							}
						}
					}
				}
			}
		}
		else if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
			if(Main.theNetwork.players.get(packet.id) != null) {
				Main.theNetwork.players.get(packet.id).x = packet.x;
			} else {
				requestInfo(connection);
			}
		} 
		else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
			if(Main.theNetwork.players.get(packet.id) != null) {
				Main.theNetwork.players.get(packet.id).y = packet.y;
			} else {
				requestInfo(connection);
			}
		} 
		else if(object instanceof PacketChatMessage) {
			PacketChatMessage packet = (PacketChatMessage) object;
			//Main.theNetwork.rawMessages.add(new ChatMessage(packet.message, false));
			if(!packet.message.startsWith(" [SERVER]")) {
				if(Main.theNetwork.players.get(packet.id) != null) {
					if(Main.theNetwork.players.get(packet.id).userName == null) {
						Main.theNetwork.players.get(packet.id).userName =  packet.message.split(">")[0].replace("<", "").replace(">","").trim();
					}
				}
				
			}
			
			/*if(Main.currentScreen instanceof GuiChat) {
				((GuiChat)Main.currentScreen).updateMessages();
			}*/
		}
		else if(object instanceof PacketUpdateAnimation) {
			PacketUpdateAnimation packet = (PacketUpdateAnimation) object;
			if(Main.theNetwork.players.get(packet.id) != null)
				Main.theNetwork.players.get(packet.id).anim = packet.anim;
		}
		else if(object instanceof PacketGameJoin) {
			PacketGameJoin packet = (PacketGameJoin) object;
			switch(packet.map) {
			case "sample":
				Main.currentLevel = new SampleLevel(true);
				break;
			}
		}
		else if(object instanceof PacketAddObject) {
			PacketAddObject packet = (PacketAddObject) object;
			System.out.println(packet.networkID);
			switch(packet.objClass) {
			case "Door":
				Door door = new Door((Key)addSpecialObject(packet.subNetworkID, packet.subObj, packet.subObjName), false, packet.x, packet.y);
				door.setNetworkID(packet.networkID);
				Main.currentLevel.addObject(door);
				break;
			case "PickableObject":
				Key key = (Key)addSpecialObject(packet.subNetworkID, packet.subObj, packet.subObjName);
				PickableObject pickable = new PickableObject(key, packet.x, packet.y);
				pickable.setNetworkID(packet.networkID);
				Main.currentLevel.addObject(pickable);
				break;
			}
		}
		else if(object instanceof PacketRemoveObject) {
			PacketRemoveObject packet = (PacketRemoveObject) object;
			try {
				for(OBJ obj : Main.currentLevel.getObjects()) {
					if(obj.networkID.contentEquals(packet.networkID)) {
						Main.currentLevel.removeObjectNoNet(obj);
					}
				}
			} catch(Exception e) {}
			
		}
		else if(object instanceof PacketUpdateDirection) {
			PacketUpdateDirection packet = (PacketUpdateDirection) object;
			if(Main.theNetwork.players.get(packet.id) != null)
				Main.theNetwork.players.get(packet.id).direction = packet.dir;
		}
	}
	
	private OBJ addSpecialObject(String networkID, String classs, String name) {
		boolean shouldadd = true;
		for(OBJ obj : specialOBJs) {
			if(obj.name.contentEquals(name) && obj.networkID.contentEquals(networkID)) {
				shouldadd = false;
				return obj;
			}
		}
		
		if(shouldadd) {
			switch(classs) {
			case "Key":
				Key key = new Key(name, -1, -1);
				key.setNetworkID(networkID);
				specialOBJs.add(key);
				return key;
			}
		}
		return null;
	}
	
	private void requestInfo(Connection connection) {
		if(!Main.theNetwork.players.keySet().contains(connection.getID())) {
			Main.theNetwork.players.put(connection.getID(), new OtherPlayer());
		}
	}
}
