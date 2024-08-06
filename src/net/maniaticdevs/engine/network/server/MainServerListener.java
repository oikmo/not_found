package net.maniaticdevs.engine.network.server;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginRequest;
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
import net.maniaticdevs.engine.network.packet.RandomNumber;
import net.maniaticdevs.engine.objects.Door;
import net.maniaticdevs.engine.objects.OBJ;
import net.maniaticdevs.engine.objects.PickableObject;

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
			MainServer.append(username + " left the server\n");
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
				
				for(OtherPlayer p : players.values()) {
					if(p.userName != null) {
						if(p.userName.contentEquals(request.getUserName())) {
							PacketRemovePlayer packetKick = new PacketRemovePlayer();
							packetKick.id = connection.getID();
							packetKick.kick = true;
							packetKick.message = "You are already on here!";
							MainServer.server.sendToAllTCP(packetKick);
							players.remove(connection.getID());
							
							MainServer.append("Kicked " + packetKick.id + " from the server. (Already exists)\n");
							return;
						}
					}
					
				}
				
				PacketUserName packetUserName = new PacketUserName();
				packetUserName.id = connection.getID();
				packetUserName.userName = request.getUserName();
				
				MainServer.server.sendToAllExceptUDP(connection.getID(), packetUserName);
				
				if(players.get(connection.getID()) == null) {
					players.put(connection.getID(), new OtherPlayer());
				}
				players.get(connection.getID()).userName = request.getUserName();
				
				if(request.getUserName() != null && !request.getUserName().isEmpty()) {
					MainServer.append(request.getUserName() + " joined the server\n");
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
					}
					connection.sendUDP(packetaddobj);
				}
				
				for (OtherPlayer p : players.values()) {
					PacketUserName packetUserName2 = new PacketUserName();
					if(p.c == null) {
						players.values().remove(p);
						continue;
					}
					packetUserName2.id = p.c.getID();
					packetUserName2.userName = p.userName;
					System.out.println(p.userName + " " + p.c.getID());
					// connection.sendTCP(packetUserName2);
					connection.sendUDP(packetUserName2);
				}
			} else {
				response.PROTOCOL = -1;
				response.setResponseText("not ok!");
				connection.sendTCP(response);
				MainServer.append("Player \" "+ request.getUserName() + " \"  could not join\nas they had a protocol version of " + request.PROTOCOL +" whilst server is " + MainServer.NETWORK_PROTOCOL + "\n");
			}
			
		}

		// random number packet, sync'd across the entire network
		RandomNumber packetRandom = new RandomNumber();
		packetRandom.randomFloat = 0.254f;
		MainServer.server.sendToTCP(connection.getID(),packetRandom);

		if(object instanceof PacketUpdateX) {
			PacketUpdateX packet = (PacketUpdateX) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).x = packet.x;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdateY) {
			PacketUpdateY packet = (PacketUpdateY) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).y = packet.y;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdateAnimation) {
			PacketUpdateAnimation packet = (PacketUpdateAnimation) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).anim = packet.anim;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketUpdateDirection) {
			PacketUpdateDirection packet = (PacketUpdateDirection) object;
			if(players.get(connection.getID()) != null)
				players.get(connection.getID()).direction = packet.dir;

			packet.id = connection.getID();
			MainServer.server.sendToAllExceptUDP(connection.getID(), packet);
		}
		else if(object instanceof PacketChatMessage) {
			PacketChatMessage packet = (PacketChatMessage) object;
			packet.id = connection.getID();
			MainServer.append(packet.message+"\n");
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
		else if(object instanceof PacketRemoveObject) {
			PacketRemoveObject packet = (PacketRemoveObject) object;
			packet.id = connection.getID();
			MainServer.server.sendToAllExceptTCP(connection.getID(), packet);
		}
	}
}