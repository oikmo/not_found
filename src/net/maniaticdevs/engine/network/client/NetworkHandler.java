package net.maniaticdevs.engine.network.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.packet.LoginRequest;
import net.maniaticdevs.engine.network.packet.LoginResponse;
import net.maniaticdevs.engine.network.packet.Message;
import net.maniaticdevs.engine.network.packet.PacketAddObject;
import net.maniaticdevs.engine.network.packet.PacketAddPlayer;
import net.maniaticdevs.engine.network.packet.PacketChatMessage;
import net.maniaticdevs.engine.network.packet.PacketGameJoin;
import net.maniaticdevs.engine.network.packet.PacketRemoveObject;
import net.maniaticdevs.engine.network.packet.PacketRemovePlayer;
import net.maniaticdevs.engine.network.packet.PacketSavePlayerPosition;
import net.maniaticdevs.engine.network.packet.PacketUpdateAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdateDirection;
import net.maniaticdevs.engine.network.packet.PacketUpdateX;
import net.maniaticdevs.engine.network.packet.PacketUpdateY;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.network.packet.RandomNumber;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.engine.util.math.Vector2;
import net.maniaticdevs.main.Main;

public class NetworkHandler {
	
	public static float rand;

	public static JFrame frame;
	public Random random = new Random();
	
	private int port = 25565;
	private int timeout = 500000;
	private String ip;
	
	public Client client;
	private static Kryo kryo;
	
	public OtherPlayer player;
	public Map<Integer, OtherPlayer> players = new HashMap<Integer, OtherPlayer>();
	
	private int tickTimer = 0;
	public static final int NETWORK_PROTOCOL = 1;
	
	private static void registerKryoClasses() {
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(Message.class);
		kryo.register(PacketAddPlayer.class);
		kryo.register(PacketChatMessage.class);
		kryo.register(PacketRemovePlayer.class);
		kryo.register(PacketSavePlayerPosition.class);
		kryo.register(PacketUpdateX.class);
		kryo.register(PacketUpdateY.class);
		kryo.register(PacketUserName.class);
		kryo.register(PacketUpdateAnimation.class);
		kryo.register(PacketUpdateDirection.class);
		kryo.register(PacketAddObject.class);
		kryo.register(PacketRemoveObject.class);
		kryo.register(PacketGameJoin.class);
		kryo.register(RandomNumber.class);
	}
	
	public NetworkHandler(String ipAddress) throws Exception {
		this.ip = ipAddress;
		players = new HashMap<Integer, OtherPlayer>();
		player = new OtherPlayer();
		player.userName = Main.playerName;
		client = new Client();
		kryo = client.getKryo();
		registerKryoClasses();
		connect(ip);
	}
	
	public static void testNetwork(String ipAddress) throws Exception {
		String ip = ipAddress;
		int port = 25565;
		int timeout = 500000;
		Client client = new Client();
		kryo = client.getKryo();
		registerKryoClasses();
		
		Logger.log(LogLevel.INFO, "Test connecting...");
		client.start();
		client.connect(timeout, ip, port, port);
		Logger.log(LogLevel.INFO, "Test connected!");
		Logger.log(LogLevel.INFO, "Test disconnecting...");
		client.stop();
		Logger.log(LogLevel.INFO, "Test disconnected.");
	}
	
	public void tick() {
		
		int lastAnimTick = player.anim;
		player.anim = Main.thePlayer.spriteNum;
		
		if(lastAnimTick != player.anim) {
			PacketUpdateAnimation packet = new PacketUpdateAnimation();
			packet.anim = player.anim;
			client.sendUDP(packet);
		}
		
		int lastDirection = player.direction;
		player.direction = Main.thePlayer.getDirection().ordinal();
		
		if(lastDirection != player.direction) {
			PacketUpdateDirection packet = new PacketUpdateDirection();
			packet.dir = player.direction;
			client.sendUDP(packet);
		}
		
		if(tickTimer <= 5) {
			tickTimer++;
		} else {
			tickTimer = 0;
			Vector2 pos = Main.thePlayer.getPosition();
			player.updatePosition(pos.x, pos.y);
			PacketUpdateX packetX = new PacketUpdateX();
			packetX.x = player.x;
			client.sendUDP(packetX);
			PacketUpdateY packetY = new PacketUpdateY();
			packetY.y = player.y;
			client.sendUDP(packetY);
			
		}
	}
	
	public void update() {
		if(!client.isConnected()) {
			Main.disconnect(false, Main.lang.translateKey("network.disconnect.ux"));
			return;
		}
		
		try {
			for(OtherPlayer p : players.values()) {
				if(p.userName.contentEquals(Main.playerName)) {
					players.remove(p.c.getID());
				}
			}
		} catch(Exception e) {}
		
		
		
		if(player.c != null) {
			if(players.containsKey(player.c.getID())) {
				players.remove(player.c.getID());
			}
		}
		
		int x = player.x;
		int y = player.y;
		Vector2 pos = Main.thePlayer.getPosition();
		player.updatePosition(pos.x, pos.y);
		
		if(x != player.x) {
			PacketUpdateX packetX = new PacketUpdateX();
			packetX.x = player.x;
			client.sendUDP(packetX);
		}
		
		if(y != player.y) {	
			PacketUpdateY packetY = new PacketUpdateY();
			packetY.y = player.y;
			client.sendUDP(packetY);
		}
	}
	
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
