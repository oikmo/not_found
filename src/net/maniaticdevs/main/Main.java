package net.maniaticdevs.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.gui.GuiScreen;
import net.maniaticdevs.engine.level.Level;
import net.maniaticdevs.engine.level.MapLoader;
import net.maniaticdevs.engine.network.OtherPlayer;
import net.maniaticdevs.engine.network.client.NetworkHandler;
import net.maniaticdevs.engine.network.server.MainServer;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.engine.util.os.EnumOS;
import net.maniaticdevs.engine.util.os.EnumOSMappingHelper;
import net.maniaticdevs.engine.util.properties.LanguageHandler;
import net.maniaticdevs.engine.util.sound.Sound;
import net.maniaticdevs.engine.web.WebServer;
import net.maniaticdevs.main.entity.Player;
import net.maniaticdevs.main.gui.GuiComputer;
import net.maniaticdevs.main.gui.GuiDialogue;
import net.maniaticdevs.main.gui.GuiDisconnected;
import net.maniaticdevs.main.gui.GuiInGame;
import net.maniaticdevs.main.gui.GuiPauseScreen;

/**
 * Main class, enters thread, is thread.
 * @author Oikmo, LYCNK
 */
public class Main extends JPanel implements Runnable  {
	/** 02/08/2022 */
	private static final long serialVersionUID = 282022L;

	/** Current instance of {@link Main} */
	private static Main instance;
	/** Returns current instance of {@link Main} 
	 * @return {@link Main}
	 */
	public static Main getInstance() {
		return instance;
	}

	/** Where gameplay is seen. */
	private static JFrame window;
	/** In case there's some <b><i>freaky</i></b> bugs! */
	public static boolean debug = false;

	/** As to be used for the Main class as it Implements Runnable which is a core for Threads in java. */
	private Thread gameThread;

	/** The player <br>cool kid and allat :3 */
	public static Player thePlayer;

	/** The active level */
	public static Level currentLevel;

	/** GuiScreen to render */
	public static GuiScreen currentScreen;

	/** Playtime tracker */
	public static long startedPlaying = System.currentTimeMillis();
	
	/** Name of player, for chatting and networking */
	public static String playerName;
	/** Sound library for Sound FX */
	public static Sound sfxLib;
	/** Network Client */
	public static NetworkHandler theNetwork;
	/** Network Server */
	public static MainServer server;
	/** Language Handler for universal texts */
	public static LanguageHandler lang = LanguageHandler.getInstance();

	public static Thread webThread;
	
	/**
	 * Opens window and starts game.
	 * @param args program arguments
	 * @throws InterruptedException incase something goes funky
	 */
	public static void main(String[] args) throws InterruptedException {
		//handles arguments given by cli
		for(String arg : args) {
			if(arg.contentEquals("-debug")) {
				debug = true;
			}
		}

		window = new JFrame("not_found"); // create window with name of "not_found"
		/* MAKES THE WINDOW BORDERLESS */
		//window.setUndecorated(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Main mainPanel = new Main();
		window.add(mainPanel); // since Main class is JPanel we can just add it here.
		window.pack();

		window.setLocationRelativeTo(null); // center window
		window.setVisible(true);
		window.setPreferredSize(new Dimension(Settings.windowWidth, Settings.windowHeight));
		window.setMinimumSize(new Dimension(Settings.windowWidth, Settings.windowHeight));

		mainPanel.runThreads();
	}

	/**
	 * Disconnects the player from a server.
	 * @param kick was it a kick from server
	 * @param message message from disconnect
	 */
	public static void disconnect(boolean kick, String message) {
		if(server != null) {
			server.stopServer();
			server = null;
		}

		try {
			if(Main.theNetwork != null) {
				Main.theNetwork.disconnect();
				if(Main.theNetwork.players != null) {
					Main.theNetwork.players.clear();
				}
				
			}
		} catch(Exception e) {}		

		Main.theNetwork = null;
		Main.currentLevel = null;
		Main.thePlayer = null;
		
		Main.currentScreen = new GuiDisconnected(kick, message);
	}

	/**
	 * Main constructor, sets ups the panel values such as size and listeners and threads
	 */
	public Main() {
		this.setPreferredSize(new Dimension(Settings.windowWidth, Settings.windowHeight));
		this.setBackground(new Color(20,20,20));
		this.setDoubleBuffered(true); // better performance
		this.addKeyListener(new Input());
		this.setFocusable(true); // so that the input class can actually work
		
		gameThread = new Thread(this);
		gameThread.setName("Main thread");

		instance = this;
		Sound.init();
		GuiScreen.init();
		MapLoader.init();
		sfxLib = new Sound("sfx/00cursor","sfx/01door","sfx/02hitmonster","sfx/03key","sfx/04powerup","sfx/05receivedamage","sfx/06swingweapon", "sfx/07catgif");

		currentScreen = new GuiComputer();
	}

	/**
	 * Starts any defined threads.
	 */
	public void runThreads() {
		gameThread.run();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while(gameThread != null) {
			long now = System.nanoTime();
			delta += (now-lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while(delta >= 1) {
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(shouldRender) {
				frames++;
				repaint();
			}

			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				window.setTitle("not_found (" + frames +"FPS)");
				frames = 0;
			}
		}
	}
	/** To prevent action to be done repeatedly in a short period of time */
	private boolean lockEscapeToGame = false;

	/**
	 * Logic function that syncs with the 60 tick interval as to maintain a constant speed
	 */
	private void tick() {
		if(Input.isKeyDown(Input.KEY_ESC)) {
			if(!lockEscapeToGame) {
				if(currentScreen instanceof GuiInGame) {
					if(((GuiInGame) currentScreen).chatScreen == null) {
						currentScreen = new GuiPauseScreen();
					} else {
						((GuiInGame) currentScreen).chatScreen = null;
					}
				} else {
					if(currentScreen instanceof GuiDialogue) {
						((GuiDialogue)currentScreen).cancelDialogue();
						
						currentScreen = new GuiInGame();
					}
				}
			}
			lockEscapeToGame = true;
		} else {
			lockEscapeToGame = false;
		}
		if(currentScreen != null) {
			currentScreen.tick();
		}
		
		if(Main.server != null) {
			Main.server.tick();
		}
		
		if(thePlayer != null) {
			thePlayer.updateScreenPos();
			thePlayer.tick();
			if(currentLevel != null) {
				currentLevel.tick(false);
			}
		}

		if(theNetwork != null && thePlayer != null) { 
			theNetwork.tick();
			try {
				for(OtherPlayer p : Main.theNetwork.players.values()) {
					p.tick();
				}
			} catch(Exception e) {}
		}
	}

	/**
	 * Draws all entities and tiles and whatnots in here!
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		try {
			if(thePlayer != null) { 
				if(currentLevel != null) {
					currentLevel.draw(g2, thePlayer.getPosition(), thePlayer.getScreenPosition());
				}
				thePlayer.draw(g2);
				
				if(theNetwork != null) {
					theNetwork.update();
					try {
						for(OtherPlayer p : Main.theNetwork.players.values()) {
							p.draw(g2, thePlayer.getPosition(), thePlayer.getScreenPosition());
						}
					} catch(Exception e) {}
				}
			}

			if(currentScreen != null) {
				currentScreen.draw(g2);
			}
		} catch(Exception e) {}

		g2.setColor(Color.WHITE);
		g2.setFont(GuiScreen.font.deriveFont(18.0F));
		g2.drawString("not_found <REMAKE> [[A1.0.1]]", 0, 18);

		g2.dispose();
	}

	/**
	 * Retrieves data directory of .blockbase/ using {@code Main.getWorkingDirectory(String)}
	 * @return Directory (File)
	 */
	public static File getWorkingDirectory() {
		return getWorkingDirectory("not_found");
	}

	/**
	 * Uses {@link EnumOSMappingHelper} to locate an APPDATA directory in the system.
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
}
