package net.maniaticdevs.engine.network.server;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import net.maniaticdevs.engine.level.Level;
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
import net.maniaticdevs.engine.util.os.EnumOS;
import net.maniaticdevs.engine.util.os.EnumOSMappingHelper;
import net.maniaticdevs.main.level.SampleLevel;

public class MainServer {

	private int tcpPort;
	private int udpPort;
	public static Server server;
	private Kryo kryo;
	public static float randomFloatNumber;

	static MainServerListener listener = new MainServerListener();
	public static int xSpawn = 0,zSpawn = 0;

	private static String[] splashes;

	private static String version = "S1.0.0";
	public static final int NETWORK_PROTOCOL = 1;

	private static Thread saveThread;
	
	public static String map;
	public static Level currentLevel;

	public MainServer(int tcpPort, String mapToLoad) {
		this.tcpPort = tcpPort;
		this.udpPort = tcpPort;
		map = mapToLoad;
		switch(mapToLoad) {
		case "sample":
			currentLevel = new SampleLevel(false);
			break;
		}
		append("----------------------------");
		server = new Server();
		splashes = new String[1];
		splashes[0] = "fuck you";
		//append(splashes[new Random().nextInt(splashes.length)]);
		kryo = server.getKryo();
		registerKryoClasses();
		Random rand = new Random();
		randomFloatNumber = rand.nextFloat();
		startServer();
		
	}

	public void startServer() {
		Logger.log(LogLevel.INFO,"Starting Server");
		append("Starting Server...\n");
		server.start();
		try {
			server.bind(tcpPort, udpPort);
			server.addListener(listener);
			append("Server online! (PORT="+ tcpPort +")\n");
			append("Don't forget to port forward 25555 for server info!\n");
			Logger.log(LogLevel.INFO, "Server online! (PORT="+ tcpPort +")");
			append("----------------------------");
			append("\n");
			saveThread = new Thread(new Runnable() {
				public void run() {
					while(true) {
						try {
							Thread.sleep(6000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			saveThread.setName("World Save Thread");
			//saveThread.start();

		} catch (IOException e) {
			Logger.log(LogLevel.INFO,"Port already used");
			append("Port already in use");
			append("\n");
			e.printStackTrace();
		}
	}

	// Try changing this to non static and see where this effects our game
	public static void stopServer() {
		Logger.log(LogLevel.INFO,"Server stopped");
		append("Server stopped.");
		append("\n");
		for (OtherPlayer p : MainServerListener.players.values()) {
			PacketRemovePlayer packetDisconnect = new PacketRemovePlayer();
			packetDisconnect.id = p.c.getID();
			packetDisconnect.message = "Server closed";
			// connection.sendTCP(packetUserName2);
			p.c.sendUDP(packetDisconnect);
		}
		//SaveSystem.saveWorldPosition("server-level", new WorldPositionData(xSpawn, zSpawn));
		server.stop();
		Logger.saveLog();
	}

	private void registerKryoClasses() {
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

	public static String getRandomSplash() {
		return splashes[new Random().nextInt(splashes.length)];
	}

	private static void handleCommand(String cmd) {
		String command = cmd.replace("/", "");

		if(command.contentEquals("help")) {
			append("\n");
			append("setSpawn - sets spawn location of server - (setSpawn <x> <z>)\n");
			append("seed - returns the seed of the world - (seed)\n");
			append("save - saves the world - (save)\n");
			append("kick - kicks a player from their ip - (kick <id> <reason>) or (kick <playerName> <reason>)\n");
			append("chunks - returns total chunk size of world - (chunks)\n");
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
				xSpawn = tempX;
				zSpawn = tempZ;
				//SaveSystem.saveWorldPosition("server-level", new WorldPositionData(xSpawn, zSpawn));

				append("Successfully set spawn position to: [X="+xSpawn+", Z="+zSpawn+"]!");
			} else {
				append("Unable to set spawn position as inputted values were invalid.\n");
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

				append("Kicked " + (noIDGiven ? toID : playerID) + " from the server.\n");

			} else {
				append("ID was not valid / Reason was not supplied\n");
			}
		} else if(command.startsWith("say ")) {
			String message = command.substring(4);
			PacketChatMessage packet = new PacketChatMessage();
			packet.message=" [SERVER] " + message;
			server.sendToAllUDP(packet);
			append(packet.message+"\n");
		} else if(command.contentEquals("players")) {
			if(MainServerListener.players.size() != 0) {
				for(Map.Entry<Integer, OtherPlayer> entry : MainServerListener.players.entrySet()) {
					append(entry.getValue().userName + " ("+entry.getKey()+")");
				}
			} else {
				append("No players!");
			}
		} else {
			append("Command \""+ cmd + "\" was not recognized!\n");
		}
	}

	/**
	 * Retrieves data directory of .blockbase/ using {@code Main.getAppDir(String)}
	 * @return Directory (File)
	 */
	public static File getWorkingDirectory() {
		return getWorkingDirectory("not_found-server");
	}

	/**
	 * Uses {@code Main.getOS} to locate an APPDATA directory in the system.
	 * Then it creates a new directory based on the given name e.g <b>.name/</b>
	 * 
	 * @param name (String)
	 * @return Directory (File)
	 */
	public static File getWorkingDirectory(String name) {
		String userDir = System.getProperty("user.home", ".");
		File folder;
		switch(EnumOSMappingHelper.os[EnumOS.getOS().ordinal()]) {
		case 1:
		case 2:
			folder = new File(userDir, '.' + name + '/');
			break;
		case 3:
			String appdataLocation = System.getenv("APPDATA");
			if(appdataLocation != null) {
				folder = new File(appdataLocation, "." + name + '/');
			} else {
				folder = new File(userDir, '.' + name + '/');
			}
			break;
		case 4:
			folder = new File(userDir, "Library/Application Support/" + name);
			break;
		default:
			folder = new File(userDir, name + '/');
		}

		if(!folder.exists() && !folder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + folder);
		} else {
			return folder;
		}
	}
	
	public static void append(String toAppend) {
		String toPrint = toAppend.replaceAll("\n", "").trim();
		if(!toPrint.isEmpty()) {
			Logger.log(LogLevel.INFO, toPrint);
		}
	}

} // end total class
