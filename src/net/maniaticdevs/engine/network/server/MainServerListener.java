package net.maniaticdevs.engine.network.server;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.maniaticdevs.engine.entity.Entity;
import net.maniaticdevs.engine.entity.NPC;
import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginRequest;
import net.maniaticdevs.engine.network.packet.LoginResponse;
import net.maniaticdevs.engine.network.packet.PacketAddEntity;
import net.maniaticdevs.engine.network.packet.PacketAddObject;
import net.maniaticdevs.engine.network.packet.PacketAddPlayer;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.network.packet.PacketGameJoin;
import net.maniaticdevs.engine.network.packet.PacketNPCLock;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateX;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateY;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.network.packet.PacketRemovePlayer;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityDirection;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerDirection;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.network.packet.RandomNumber;
import net.maniaticdevs.engine.objects.DataBuffer;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.objects.PickableObject;
import net.maniaticdevs.main.Main;

public class MainServerListener extends Listener {

	public static Map<Integer, OtherPlayer> players = new HashMap<Integer, OtherPlayer>();

	public void connected(Connection connection) {
		OtherPlayer player = new OtherPlayer();
		player.c = connection;

		// add packet or init packet
		PacketAddPlayer addPacket = new PacketAddPlayer();
		addPacket.id = connection.getID();
		MainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);

		players.put(connection.getID(), player);
		
		for (OtherPlayer p : players.values()) {
			PacketAddPlayer addPacket2 = new PacketAddPlayer();
			addPacket2.id = p.c.getID();
			connection.sendTCP(addPacket2);
		}
	}

	public void disconnected(Connection connection) {
		String username = "";
		if(players.get(connection.getID()) != null) {
			username = players.get(connection.getID()).userName;
		}
		
		PacketRemovePlayer removePacket = new PacketRemovePlayer();
		
		removePacket.id = connection.getID();
		removePacket.kick = false;
		removePacket.message = "";
		
		players.remove(connection.getID());
		
		MainServer.server.sendToAllUDP(removePacket);
		if(username != null && !username.isEmpty()) {
			Main.server.append(username + " left the server");
		}
	}
	
	public void received(Connection connection, Object object) {		
		if(object instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) object;
			LoginResponse response = new LoginResponse();
			
			if(request.PROTOCOL == MainServer.NETWORK_PROTOCOL) {
				response.PROTOCOL = MainServer.NETWORK_PROTOCOL;
				response.setResponseText("ok");
				connection.sendTCP(response);
				
				/*for(OtherPlayer p : players.values()) {
					if(p.userName != null) {
						if(p.userName.contentEquals(request.getUserName())) {
							PacketRemovePlayer packetKick = new PacketRemovePlayer();
							packetKick.id = connection.getID();
							packetKick.kick = true;
							packetKick.message = "You are already on here!";
							MainServer.server.sendToAllTCP(packetKick);
							players.remove(connection.getID());
							
							Main.server.append("Kicked " + packetKick.id + " from the server. (Already exists)");
							return;
						}
					}
					
				}*/
				
				PacketUserName packetUserName = new PacketUserName();
				packetUserName.id = connection.getID();
				packetUserName.firstJoin = true;
				packetUserName.userName = request.getUserName();
				
				MainServer.server.sendToAllExceptUDP(connection.getID(), packetUserName);
				
				if(players.get(connection.getID()) == null) {
					players.put(connection.getID(), new OtherPlayer());
				}
				players.get(connection.getID()).userName = request.getUserName();
				
				if(request.getUserName() != null && !request.getUserName().isEmpty()) {
					Main.server.append(request.getUserName() + " joined the server");
				}
				
				PacketGameJoin packetGameJoin = new PacketGameJoin();
				packetGameJoin.map = MainServer.map;
				connection.sendTCP(packetGameJoin);
				
				if(request.getUserName().length() > 20) {
					response.PROTOCOL = -1;
					response.setResponseText("not ok!");
					connection.sendTCP(response);
				}
				for(OBJ obj : MainServer.currentLevel.getObjects()) {
					PacketAddObject packetaddobj = new PacketAddObject();
					packetaddobj.networkID = obj.networkID;
					packetaddobj.objClass = obj.getClass().getSimpleName();
					packetaddobj.x = obj.position.x;
					packetaddobj.y = obj.position.y;
					
					if(obj instanceof Door) {
						Door door = (Door) obj;
						packetaddobj.subObj = door.getRequiredKey().getClass().getSimpleName();
						packetaddobj.subObjName = door.getRequiredKey().name;
						packetaddobj.subNetworkID = door.getRequiredKey().networkID;
					} else if(obj instanceof PickableObject) {
						PickableObject pickable = (PickableObject) obj;
						packetaddobj.subObj = pickable.getItem().getClass().getSimpleName();
						packetaddobj.subObjName = pickable.getItem().name;
						packetaddobj.subNetworkID = pickable.getItem().networkID;
					} else if(obj instanceof DataBuffer) {
						DataBuffer buffer = (DataBuffer) obj;
						packetaddobj.subObj = buffer.bufferName;
						packetaddobj.subNetworkID = Integer.toString(buffer.direction);
					}
					connection.sendUDP(packetaddobj);
				}
				
				for(Entity ent : MainServer.currentLevel.getEntities()) {
					PacketAddEntity packetaddentity = new PacketAddEntity();
					packetaddentity.networkID = ent.networkID;
					packetaddentity.entityClass = ent.getClass().getSimpleName();
					packetaddentity.x = ent.getPosition().x;
					packetaddentity.y = ent.getPosition().y;
					connection.sendUDP(packetaddentity);
				}
				
				for (OtherPlayer p : players.values()) {
					PacketUserName packetUserName2 = new PacketUserName();
					if(p.c == null) {
						players.values().remove(p);
						continue;
					}
					packetUserName2.id = p.c.getID();
					packetUserName2.userName = p.userName;
					packetUserName2.firstJoin = false;
					connection.sendUDP(packetUserName2);
				}
			} else {
				response.PROTOCOL = -1;
				response.setResponseText("not ok!");
				connection.sendTCP(response);
				Main.server.append("Player \" "+ request.getUserName() + " \"  could not join\nas they had a protocol version of " + request.PROTOCOL +" whilst server is " + MainServer.NETWORK_PROTOCOL);
			}
			
		}

		// random number packet, sync'd across the entire network
		RandomNumber packetRandom = new RandomNumber();
		packetRandom.randomFloat = 0.254f;
		MainServer.server.sendToTCP(connection.getID(),packetRandom);

		if(object instanceof PacketPlayerUpdateX) {
			PacketPlayerUpdateX packet = (PacketPlayerUpdateX) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).x = packet.x;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketPlayerUpdateY) {
			PacketPlayerUpdateY packet = (PacketPlayerUpdateY) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).y = packet.y;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdatePlayerAnimation) {
			PacketUpdatePlayerAnimation packet = (PacketUpdatePlayerAnimation) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).anim = packet.anim;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdatePlayerDirection) {
			PacketUpdatePlayerDirection packet = (PacketUpdatePlayerDirection) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).direction = packet.dir;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketChatMessage) {
			PacketChatMessage packet = (PacketChatMessage) object;
			packet.id = connection.getID();
			Main.server.append("Recieved message: " + packet.message);
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
		else if(object instanceof PacketRemoveObject) {
			PacketRemoveObject packet = (PacketRemoveObject) object;
			packet.id = connection.getID();
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdateEntityAnimation) {
			MainServer.server.sendToAllTCP((PacketUpdateEntityAnimation) object);
		}
		else if(object instanceof PacketUpdateEntityDirection) {
			MainServer.server.sendToAllTCP((PacketUpdateEntityDirection) object);
		}
		else if(object instanceof PacketNPCLock) {
			PacketNPCLock packet = (PacketNPCLock) object;
			
			for(Entity ent : MainServer.currentLevel.getEntities()) {
				if(ent.networkID.contentEquals(packet.networkID)) {
					((NPC)ent).lock = packet.lock;
					break;
				}
			}
		}
	}
}