package net.maniaticdevs.engine.network.server;

import java.io.IOException;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import net.maniaticdevs.engine.level.Level;
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
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAliveState;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerAnimation;
import net.maniaticdevs.engine.network.packet.PacketUpdatePlayerDirection;
import net.maniaticdevs.engine.network.packet.PacketUserName;
import net.maniaticdevs.engine.network.packet.RandomNumber;
import net.maniaticdevs.engine.util.Logger;
import net.maniaticdevs.engine.util.Logger.LogLevel;
import net.maniaticdevs.main.level.SampleLevel;

/**
 * Server program
 * @author Oikmo
 */
public class MainServer {
	
	/** For Version sakes, to make sure players are on the right version when joining */
	public static final int NETWORK_PROTOCOL = 1;
	/** Server Version (Debug reasons or something) */
	private static String version = "S1.0.0";
	
	/** Kryo Server */
	public static Server server;
	/** Kryo Networking :D */
	private Kryo kryo;
	
	/** Port that server is running on... */
	private int port;
	
	/** Packet Listener */
	private static MainServerListener listener = new MainServerListener();
	
	/** Currently Loaded map */
	public static Level currentLevel;

	/** 
	 * Creates and starts server
	 * @param port What port the server should run on
	 * @param mapToLoad What map the server should load
	 */
	public MainServer(int port, String mapToLoad) {
		this.port = port;
		
		switch(mapToLoad) {
			case "sample":
				currentLevel = new SampleLevel(false);
				break;
		}
		append("----------------------------");
		
		server = new Server();
		kryo = server.getKryo();
		registerKryoClasses();
		startServer();
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
		kryo.register(PacketUpdatePlayerAliveState.class);
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
	 * Starts server
	 */
	public void startServer() {
		append("Starting Server ("+version+") ...");
		server.start();
		try {
			server.bind(port, port);
			server.addListener(listener);
			append("Server online! (PORT="+ port +")");
			append("----------------------------");
		} catch (IOException e) {
			append("Port already in use");
			e.printStackTrace();
		}
	}
	
	/** 
	 * Logic function (called by {@link net.maniaticdevs.main.Main})
	 */
	public void tick() {
		currentLevel.tick(true);
	}
	
	/**
	 * Stops server, disconnects all players before doing so.
	 */
	public void stopServer() {
		append("Server stopped.");
		PacketRemovePlayer packetDisconnect = new PacketRemovePlayer();
		packetDisconnect.message = "Server closed";
		server.sendToAllUDP(packetDisconnect);
		server.stop();
	}

	
	/**
	 * Runs commands
	 * @param cmd Command to be parsed
	 */
	@SuppressWarnings("unused")
	public void handleCommand(String cmd) {
		String command = cmd.replace("/", "");

		if(command.contentEquals("help")) {
			append("setSpawn - sets spawn location of server - (setSpawn <x> <z>)");
			append("save - saves the world - (save)");
			append("kick - kicks a player from their ip - (kick <id> <reason>) or (kick <playerName> <reason>)");
			append("players - see every player on server - (players)");
		} else if(command.startsWith("setSpawn ")) {
			String[] split = cmd.split(" ");
			boolean continueToDoStuff = true;
			String toX = null;
			String toZ = null;

			int tempX = Integer.MIN_VALUE;
			int tempZ = Integer.MIN_VALUE;
			try {
				toX = split[1];
				toZ = split[2];
			} catch(ArrayIndexOutOfBoundsException e) {
				continueToDoStuff = false;
			}

			try {
				tempX = Integer.valueOf(toX);
				tempZ = Integer.valueOf(toZ);
			} catch(NumberFormatException e) {
				continueToDoStuff = false;
			}

			if(continueToDoStuff) {
				//xSpawn = tempX;
				//zSpawn = tempZ;
				//SaveSystem.saveWorldPosition("server-level", new WorldPositionData(xSpawn, zSpawn));

				//append("Successfully set spawn position to: [X="+xSpawn+", Z="+zSpawn+"]!");
			} else {
				append("Unable to set spawn position as inputted values were invalid.");
			}
		} else if(command.contentEquals("save")) {
			//theWorld.saveWorld();
			append("Saved world!");
		} else if(command.startsWith("kick ")) {
			String[] split = cmd.split(" ");
			boolean continueToDoStuff = true;
			boolean noIDGiven = false;
			String toID = null;
			String message = null;

			int playerID = Integer.MIN_VALUE;
			try {
				toID = split[1];
				message = split[2];
			} catch(ArrayIndexOutOfBoundsException e) {
				continueToDoStuff = false;
			}

			try {
				playerID = Integer.valueOf(toID);
			} catch(NumberFormatException e) {
				noIDGiven = true;
			}

			if(message == null) {
				continueToDoStuff = false;
			}

			if(MainServerListener.players.get(playerID) == null) {
				continueToDoStuff = false;
			}

			continueToDoStuff = false;
			for(Map.Entry<Integer, OtherPlayer> entry : MainServerListener.players.entrySet()) {
				OtherPlayer p = entry.getValue();

				if(p.userName.contentEquals(toID)) {
					playerID = entry.getKey();
					continueToDoStuff = true;
				}
			}

			if(continueToDoStuff) {
				System.out.println("kick " + (noIDGiven ? toID : playerID) + " ");

				String reason = cmd.split("kick " + (noIDGiven ? toID : playerID) + " ")[1];

				PacketRemovePlayer packetKick = new PacketRemovePlayer();
				packetKick.id = playerID;
				packetKick.kick = true;
				packetKick.message = reason;
				MainServer.server.sendToAllTCP(packetKick);

				append("Kicked " + (noIDGiven ? toID : playerID) + " from the server.");

			} else {
				append("ID was not valid / Reason was not supplied");
			}
		} else if(command.startsWith("say ")) {
			String message = command.substring(4);
			PacketChatMessage packet = new PacketChatMessage();
			packet.message=" [SERVER] " + message;
			server.sendToAllUDP(packet);
			append(packet.message+"");
		} else if(command.contentEquals("players")) {
			if(MainServerListener.players.size() != 0) {
				for(Map.Entry<Integer, OtherPlayer> entry : MainServerListener.players.entrySet()) {
					append(entry.getValue().userName + " ("+entry.getKey()+")");
				}
			} else {
				append("No players!");
			}
		} else {
			append("Command \""+ cmd + "\" was not recognized!");
		}
	}
	
	/**
	 * Logging
	 * @param toAppend To add
	 */
	public void append(String toAppend) {
		Logger.log(LogLevel.INFO, toAppend);
	}
}