package net.maniaticdevs.engine.network.client;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginRequest;
import net.maniaticdevs.engine.network.packet.LoginResponse;
import net.maniaticdevs.engine.network.packet.PacketAddEntity;
import net.maniaticdevs.engine.network.packet.PacketAddObject;
import net.maniaticdevs.engine.network.packet.PacketAddPlayer;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateX;
import net.maniaticdevs.engine.network.packet.PacketEntityUpdateY;
import net.maniaticdevs.engine.network.packet.PacketGameJoin;
import net.maniaticdevs.engine.network.packet.PacketNPCLock;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateX;
import net.maniaticdevs.engine.network.packet.PacketPlayerUpdateY;
import net.maniaticdevs.engine.network.packet.PacketRemoveEntity;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.network.packet.PacketRemovePlayer;
import net.maniaticdevs.engine.network.packet.PacketSavePlayerPosition;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateEntityDirection;
import net.maniaticdevs.engine.network.packet.PacketUpdateObjectAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerDirection;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.network.packet.RandomNumber;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

/**
 * Client side program
 * @author Oikmo
 */
public class NetworkHandler {
	/** For Version sakes, to make sure players are on the right version when joining */
	public static final int NETWORK_PROTOCOL = 1;
	
	/** Kryo Client */
	public Client client;
	/** Kryo Networking :D */
	private Kryo kryo;
	
	/** Port to connect to server with */
	private int port = 25565;
	/** Packet timeouts, too long? Disconnect. */
	private int timeout = 5000;
	
	/** Other connected players from server */
	public Map<Integer, OtherPlayer> players = new HashMap<Integer, OtherPlayer>();
	/** Player data */
	public OtherPlayer player;
	
	/** To show the "Server closed" message */
	public boolean stopUpdating = false;
	/** Updates the position of the player */
	private int tickUpdatePositionTimer = 0;
	
	/**
	 * Connects to server on creation
	 * @param ipAddress IP to connect to server with
	 * @throws Exception if it can't connect, make something up!
	 */
	public NetworkHandler(String ipAddress) throws Exception {
		players = new HashMap<Integer, OtherPlayer>();
		player = new OtherPlayer();
		player.userName = Main.playerName;
		client = new Client();
		kryo = client.getKryo();
		registerKryoClasses();
		connect(ipAddress);
	}
	
	/**
	 * Registers all classes in {@link net.maniaticdevs.engine.network.packet}
	 */
	private void registerKryoClasses() {
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(PacketAddPlayer.class);
		kryo.register(PacketChatMessage.class);
		kryo.register(PacketRemovePlayer.class);
		kryo.register(PacketSavePlayerPosition.class);
		kryo.register(PacketPlayerUpdateX.class);
		kryo.register(PacketPlayerUpdateY.class);
		kryo.register(PacketUserName.class);
		kryo.register(PacketUpdatePlayerAnimation.class);
		kryo.register(PacketUpdatePlayerDirection.class);
		kryo.register(PacketAddObject.class);
		kryo.register(PacketUpdateObjectAnimation.class);
		kryo.register(PacketRemoveObject.class);
		kryo.register(PacketGameJoin.class);
		kryo.register(PacketAddEntity.class);
		kryo.register(PacketRemoveEntity.class);
		kryo.register(PacketUpdateEntityAnimation.class);
		kryo.register(PacketUpdateEntityDirection.class);
		kryo.register(PacketEntityUpdateX.class);
		kryo.register(PacketEntityUpdateY.class);
		kryo.register(PacketNPCLock.class);
		kryo.register(RandomNumber.class);
	}
	
	/** 
	 * Logic function (called by {@link net.maniaticdevs.main.Main})
	 * 
	 * Handles player animation and direction.
	 */
	public void tick() {
		int lastAnimTick = player.anim;
		player.anim = Main.thePlayer.spriteNum;
		
		if(lastAnimTick != player.anim) {
			PacketUpdatePlayerAnimation packet = new PacketUpdatePlayerAnimation();
			packet.anim = player.anim;
			client.sendUDP(packet);
		}
		
		int lastDirection = player.direction;
		player.direction = Main.thePlayer.getDirection().ordinal();
		
		if(lastDirection != player.direction) {
			PacketUpdatePlayerDirection packet = new PacketUpdatePlayerDirection();
			packet.dir = player.direction;
			client.sendUDP(packet);
		}
		
		if(tickUpdatePositionTimer <= 20) {
			tickUpdatePositionTimer++;
		} else {
			tickUpdatePositionTimer = 0;
			Vector2 pos = Main.thePlayer.getPosition();
			player.updatePosition(pos.x, pos.y);
			PacketPlayerUpdateX packetX = new PacketPlayerUpdateX();
			packetX.x = player.x;
			client.sendUDP(packetX);
			PacketPlayerUpdateY packetY = new PacketPlayerUpdateY();
			packetY.y = player.y;
			client.sendUDP(packetY);
		}
	}
	
	/**
	 * Runs freely, handles positioning.
	 */
	public void update() {
		if(stopUpdating) {
			return;
		}
		if(!client.isConnected()) {
			Main.disconnect(false, Main.lang.translateKey("network.disconnect.ux"));
			return;
		}
		
		try {
			for(OtherPlayer p : players.values()) {
				if(p.userName.contentEquals(Main.playerName)) {
					players.remove(p.id);
				}
			}
		} catch(Exception e) {}
		
		if(client != null) {
			if(players.containsKey(client.getID())) {
				players.remove(client.getID());
			}
		}
		
		int x = player.x;
		int y = player.y;
		Vector2 pos = Main.thePlayer.getPosition();
		player.updatePosition(pos.x, pos.y);
		
		if(x != player.x) {
			PacketPlayerUpdateX packetX = new PacketPlayerUpdateX();
			packetX.x = player.x;
			client.sendUDP(packetX);
		}
		
		if(y != player.y) {	
			PacketPlayerUpdateY packetY = new PacketPlayerUpdateY();
			packetY.y = player.y;
			client.sendUDP(packetY);
		}
	}
	
	/**
	 * Connects to server
	 * @param ip IP to connect to server with
	 * @throws Exception if it can't connect, make something up!
	 */
	public void connect(String ip) throws Exception {
		Logger.log(LogLevel.INFO, "Connecting...");
		client.start();
		client.connect(timeout, ip, port, port);
		client.addListener(new PlayerClientListener());
		players = new HashMap<Integer, OtherPlayer>();
		LoginRequest request = new LoginRequest();
		request.setUserName(player.userName);
		request.PROTOCOL = NetworkHandler.NETWORK_PROTOCOL;
		client.sendTCP(request);
		
		Logger.log(LogLevel.INFO, "Connected.");
	}
	
	/**
	 * Sends a {@link PacketSavePlayerPosition} packet before disconnecting (100ms wait before doing so as to send packet)
	 */
	public void disconnect()  {
		if(Main.thePlayer != null) {
			PacketSavePlayerPosition data = new PacketSavePlayerPosition();
			data.userName = Main.playerName;
			data.x = Main.thePlayer.getPosition().x;
			data.y = Main.thePlayer.getPosition().y;
			client.sendTCP(data);
		}
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Logger.log(LogLevel.INFO, "Disconnecting...");
		client.stop();
		Logger.log(LogLevel.INFO, "Disconnected.");
	}
}
