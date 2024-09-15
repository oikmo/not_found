package net.maniaticdevs.engine.network.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.EntityDirection;
import net.maniaticdevs.engine.network.ChatMessage;
import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginResponse;
import net.maniaticdevs.engine.network.packet.PacketAddEntity;
import net.maniaticdevs.engine.network.packet.PacketAddObject;
import net.maniaticdevs.engine.network.packet.PacketAddPlayer;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateX;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateY;
import net.maniaticdevs.engine.network.packet.PacketGameJoin;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateX;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateY;
import net.maniaticdevs.engine.network.packet.PacketRemoveEntity;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.network.packet.PacketRemovePlayer;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityDirection;
import net.maniaticdevs.engine.network.packet.PacketUpdateObjectAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAliveState;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerDirection;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.objects.Computer;
import net.maniaticdevs.engine.objects.DataBuffer;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.Key;
import net.maniaticdevs.engine.objects.MovingImage;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;
import net.maniaticdevs.main.entity.Test;
import net.maniaticdevs.main.entity.Watcher;
import net.maniaticdevs.main.gui.GuiChat;
import net.maniaticdevs.main.gui.GuiInGame;
import net.maniaticdevs.main.level.SampleLevel;

/**
 * Packet listener for client
 * @author Oikmo
 */
public class PlayerClientListener extends Listener {

	/** Special objs to be added to the level */
	public static List<OBJ> specialOBJs = new ArrayList<>();
	/** Objs to be added to the level */
	private static List<OBJ> needsToBeAddedObjs = new ArrayList<>();
	/** Entities to be added to the level */
	private static List<Entity> needsToBeAddedEntities = new ArrayList<>(); 

	public void received(Connection connection, Object object) {
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
			if(packet.id == Main.theNetwork.client.getID() || packet.message.contentEquals("Server closed")) {
				Main.theNetwork.stopUpdating = true;
				Main.disconnect(packet.kick, packet.message);
			} else {
				if(Main.thePlayer != null) {
					if(Main.theNetwork.players.get(packet.id) != null && Main.theNetwork.players.get(packet.id).userName != null) {
						if(!Main.theNetwork.players.get(packet.id).userName.contentEquals(Main.theNetwork.player.userName)) {
							GuiChat.originalMessages.add(Main.theNetwork.players.get(packet.id).userName + " left the game");
							if(Main.currentScreen instanceof GuiInGame) {
								if(((GuiInGame)Main.currentScreen).chatScreen != null) {
									((GuiInGame)Main.currentScreen).chatScreen.recompileMessages();
								}
							}
						}
					}
				}
				Main.theNetwork.players.remove(packet.id);
			}
		}
		else if(object instanceof PacketUserName){
			PacketUserName packet = (PacketUserName) object;
			if(Main.theNetwork == null) {
				return;
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
							if(packet.firstJoin) {
								GuiChat.originalMessages.add(packet.userName + " joined the game");
								if(Main.currentScreen instanceof GuiInGame) {
									if(((GuiInGame)Main.currentScreen).chatScreen != null) {
										((GuiInGame)Main.currentScreen).chatScreen.recompileMessages();
									}
								}
							}

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
							if(packet.firstJoin) {
								GuiChat.originalMessages.add(packet.userName + " joined the game");
								if(Main.currentScreen instanceof GuiInGame) {
									if(((GuiInGame)Main.currentScreen).chatScreen != null) {
										((GuiInGame)Main.currentScreen).chatScreen.recompileMessages();
									}
								}
							}
							if(Main.theNetwork.players.get(packet.id).userName == null) {
								Main.theNetwork.players.get(packet.id).userName =  packet.userName;	
							}
						}
					}
				}
			}
		}
		else if(object instanceof PacketPlayerUpdateX){
			PacketPlayerUpdateX packet = (PacketPlayerUpdateX) object;
			if(Main.theNetwork.players.get(packet.id) != null) {
				Main.theNetwork.players.get(packet.id).x = packet.x;
			}
		} 
		else if(object instanceof PacketPlayerUpdateY){
			PacketPlayerUpdateY packet = (PacketPlayerUpdateY) object;
			if(Main.theNetwork.players.get(packet.id) != null) {
				Main.theNetwork.players.get(packet.id).y = packet.y;
			}
		} 
		else if(object instanceof PacketChatMessage) {
			PacketChatMessage packet = (PacketChatMessage) object;
			packet.message = packet.message.trim();
			packet.message = packet.message.replace("\n", "");
			GuiChat.originalMessages.add("<"+Main.theNetwork.players.get(packet.id).userName+"> "+packet.message);
			
			if(Main.theNetwork.players.get(packet.id) != null) {
				new ChatMessage(Main.theNetwork.players.get(packet.id).messages, packet.message);
			}
			
			if(!packet.message.startsWith(" [SERVER]")) {
				if(Main.theNetwork.players.get(packet.id) != null) {
					if(Main.theNetwork.players.get(packet.id).userName == null) {
						Main.theNetwork.players.get(packet.id).userName =  packet.message.split(">")[0].replace("<", "").replace(">","").trim();
					}
				}
			}

			if(Main.currentScreen instanceof GuiInGame) {
				if(((GuiInGame)Main.currentScreen).chatScreen != null) {
					((GuiInGame)Main.currentScreen).chatScreen.recompileMessages();
				}
			}
		}
		else if(object instanceof PacketUpdatePlayerAnimation) {
			PacketUpdatePlayerAnimation packet = (PacketUpdatePlayerAnimation) object;
			if(Main.theNetwork.players.get(packet.id) != null)
				Main.theNetwork.players.get(packet.id).anim = packet.anim;
		}
		else if(object instanceof PacketUpdatePlayerDirection) {
			PacketUpdatePlayerDirection packet = (PacketUpdatePlayerDirection) object;
			if(Main.theNetwork.players.get(packet.id) != null)
				Main.theNetwork.players.get(packet.id).direction = packet.dir;
		}
		else if(object instanceof PacketUpdatePlayerAliveState) {
			PacketUpdatePlayerAliveState packet = (PacketUpdatePlayerAliveState) object;
			if(Main.theNetwork.players.get(packet.id) != null)
				Main.theNetwork.players.get(packet.id).dead = packet.dead;
			
			if(packet.id == Main.theNetwork.client.getID()) {
				Main.thePlayer.health = 3;
			}
		}
		else if(object instanceof PacketGameJoin) {
			PacketGameJoin packet = (PacketGameJoin) object;
			switch(packet.map) {
			case "sample":
				Main.currentLevel = new SampleLevel(true);
				break;
			}

			for(int i = 0; i < needsToBeAddedObjs.size(); i++) {
				Main.currentLevel.addObject(needsToBeAddedObjs.get(i));
				needsToBeAddedObjs.remove(needsToBeAddedObjs.get(i));
			}
			
			for(int i = 0; i < needsToBeAddedEntities.size(); i++) {
				Main.currentLevel.addEntity(needsToBeAddedEntities.get(i));
				needsToBeAddedEntities.remove(needsToBeAddedEntities.get(i));
			}
		}
		else if(object instanceof PacketAddObject) {
			PacketAddObject packet = (PacketAddObject) object;
			switch(packet.objClass) {
			case "Door":
				Door door = new Door((Key)addSpecialObject(packet.subNetworkID, packet.subObj, packet.subObjName), false, packet.x, packet.y);
				door.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addObject(door);
				} else {
					needsToBeAddedObjs.add(door);
				}
				break;
			case "PickableObject":
				Key key = (Key)addSpecialObject(packet.subNetworkID, packet.subObj, packet.subObjName);
				PickableObject pickable = new PickableObject(key, packet.x, packet.y);
				pickable.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addObject(pickable);
				} else {
					needsToBeAddedObjs.add(pickable);
				}
				break;
			case "DataBuffer":
				DataBuffer buffer = new DataBuffer(packet.subObj, Integer.parseInt(packet.subNetworkID), packet.x, packet.y);
				buffer.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addObject(buffer);
				} else {
					needsToBeAddedObjs.add(buffer);
				}
				break;
			case "MovingImage":
				String[] intData = packet.subNetworkID.split(",");
				MovingImage movingImage = new MovingImage(Integer.parseInt(packet.subObj), packet.subObjName, Integer.parseInt(intData[0]), Integer.parseInt(intData[1]), Integer.parseInt(intData[2]), packet.x, packet.y);
				movingImage.setNetworkID(packet.networkID);
				movingImage.setImageIndex(Integer.parseInt(intData[3]));
				if(Main.currentLevel != null) {
					Main.currentLevel.addObject(movingImage);
				} else {
					needsToBeAddedObjs.add(movingImage);
				}
				break;
			case "Computer":
				Computer computer = new Computer(packet.x, packet.y);
				computer.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addObject(computer);
				} else {
					needsToBeAddedObjs.add(computer);
				}
				break;
			}
		}
		else if(object instanceof PacketUpdateObjectAnimation) {
			PacketUpdateObjectAnimation packet = (PacketUpdateObjectAnimation) object;
			if(Main.currentLevel == null) {
				return;
			}
			for(OBJ obj : Main.currentLevel.getObjects()) {
				if(obj.networkID.contentEquals(packet.networkID)) {
					if(obj instanceof PickableObject) {
						((PickableObject)obj).setImageIndex(packet.anim);
						break;
					}
				}
			}
		}
		else if(object instanceof PacketRemoveObject) {
			PacketRemoveObject packet = (PacketRemoveObject) object;
			try {
				for(OBJ obj : Main.currentLevel.getObjects()) {
					if(obj.networkID.contentEquals(packet.networkID)) {
						Main.currentLevel.removeObjectNoNet(obj);
						break;
					}
				}
			} catch(Exception e) {}
		}
		else if(object instanceof PacketAddEntity) {
			PacketAddEntity packet = (PacketAddEntity) object;
			switch(packet.entityClass) {
			case "Test":
				Test npc = new Test(new Vector2(packet.x, packet.y));
				npc.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addEntity(npc);
				} else {
					needsToBeAddedEntities.add(npc);
				}
				break;
			case "Watcher":
				Watcher watcher = new Watcher(new Vector2(packet.x, packet.y));
				watcher.setNetworkID(packet.networkID);
				if(Main.currentLevel != null) {
					Main.currentLevel.addEntity(watcher);
				} else {
					needsToBeAddedEntities.add(watcher);
				}
				break;
			}
		}
		else if(object instanceof PacketRemoveEntity) {
			PacketRemoveEntity packet = (PacketRemoveEntity) object;
			try {
				for(Entity entity : Main.currentLevel.getEntities()) {
					if(entity.networkID.contentEquals(packet.networkID)) {
						Main.currentLevel.removeEntityNoNet(entity);
						break;
					}
				}
			} catch(Exception e) {}
		}
		else if(object instanceof PacketEntityUpdateX){
			PacketEntityUpdateX packet = (PacketEntityUpdateX) object;
			if(Main.currentLevel == null) {
				return;
			}
			for(Entity ent : Main.currentLevel.getEntities()) {
				if(ent.networkID.contentEquals(packet.networkID)) {
					ent.getPosition().x = packet.x;
					break;
				}
			}
		} 
		else if(object instanceof PacketEntityUpdateY){
			PacketEntityUpdateY packet = (PacketEntityUpdateY) object;
			if(Main.currentLevel == null) {
				return;
			}
			for(Entity ent : Main.currentLevel.getEntities()) {
				if(ent.networkID.contentEquals(packet.networkID)) {
					ent.getPosition().y = packet.y;
					break;
				}
			}
		} 
		else if(object instanceof PacketUpdateEntityAnimation) {
			PacketUpdateEntityAnimation packet = (PacketUpdateEntityAnimation) object;
			if(Main.currentLevel == null) {
				return;
			}
			for(Entity ent : Main.currentLevel.getEntities()) {
				if(ent.networkID.contentEquals(packet.networkID)) {
					ent.spriteNum = packet.anim;
					break;
				}
			}
		}
		else if(object instanceof PacketUpdateEntityDirection) {
			PacketUpdateEntityDirection packet = (PacketUpdateEntityDirection) object;
			if(Main.currentLevel == null) {
				return;
			}
			for(Entity ent : Main.currentLevel.getEntities()) {
				if(ent.networkID.contentEquals(packet.networkID)) {
					ent.setDirection(EntityDirection.values()[packet.dir]);
					break;
				}
			}
		}
	}
	
	/**
	 * Any important objects get processed here as to only appear once EVER.
	 * @param networkID ID of special object
	 * @param classs Class of special object
	 * @param name Name of special object
	 * @return {@link OBJ}
	 */
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
}
