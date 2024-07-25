package net.maniaticdevs.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.maniaticdevs.engine.Settings;
import net.maniaticdevs.engine.util.Input;
import net.maniaticdevs.main.entity.Player;

/**
 * Main class, enters thread, is thread.
 * 
 * @author Oikmo, LYCNK
 *
 */

public class Main extends JPanel implements Runnable  {
	/** 02/08/2022 */
	private static final long serialVersionUID = 282022L;
	
	/** Where gameplay is seen. */
	private static JFrame window;
	/** In case there's some <b><i>freaky</i></b> bugs! */
	public static boolean debug = false;
	
	/** As to be used for the Main class as it Implements Runnable which is a core for Threads in java. */
	private Thread gameThread;
	
	/** Yo!!! */
	private Player player;
	
	/**
	 * Starts threads and opens window.
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		//handles arguments given by cli
		for(String arg : args) {
			if(arg.contentEquals("-debug")) {
				debug = true;
			}
		}
		
		window = new JFrame("not_found"); // create window with name of "not_found"
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false); // for now
		Main mainPanel = new Main();
		window.add(mainPanel); // since Main class is JPanel we can just add it here.
		window.pack();
		window.setLocationRelativeTo(null); // center window
		window.setVisible(true);
		
		mainPanel.runThreads();
	}
	
	public Main() {
		this.setPreferredSize(new Dimension(Settings.windowWidth, Settings.windowHeight));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true); // better performance
		this.addKeyListener(new Input());
		this.setFocusable(true); // so that the input class can actually work
		
		player = new Player(); // would you look at that
		
		gameThread = new Thread(this);
		gameThread.setName("Main thread");
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
	
	/**
	 * Logic function that syncs with the 60 tick interval as to maintain a constant speed
	 */
	private void tick() {
		player.tick();
	}
	
	/**
	 * Draws all entities and tiles and whatnots in here!
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
		
        player.draw(g2);
        
		g2.dispose();
	}

}
