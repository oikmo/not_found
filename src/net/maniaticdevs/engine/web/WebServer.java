package net.maniaticdevs.engine.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.GZIPInputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.maniaticdevs.engine.ResourceLoader;

/**
 * Runs a website at a port... Wait what the fuck is this doing here?<br>
 * I didn't implement this??? Is this some fucking joke?
 * @author not Oikmo
 */
public class WebServer implements Runnable {
	
	/** All html pages, indexed with a label */
	private static Map<String, String> pages = new HashMap<>();
	/** All image, indexed with a label */
	private static Map<String, byte[]> images = new HashMap<>();
	/** The port that it runs the server on */
	private int webport;
	
	/**
	 * HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. HATE. 
	 */
	public WebServer() {
		this.webport = ThreadLocalRandom.current().nextInt(12860, 65534);
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns {@link #webport}
	 * @return Integer
	 */
	public int getPort() {
		return webport;
	}
	
	public void run() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(webport), 0);
			server.createContext("/", new ServerHttpHandler("index", "/"));
			server.createContext("/test.html", new ServerHttpHandler("test", "/test.html"));
			
			/* Image instantiation */
			server.createContext("/smile.png", new ServerImageHttpHandler("smile.png"));
			server.createContext("/favicon.png", new ServerImageHttpHandler("favicon.png"));
			server.createContext("/matrix.gif", new ServerImageHttpHandler("matrix.gif"));
			server.setExecutor(null);
			server.start();

			System.out.println(String.format("Website is running on port %s", webport));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the compressed file and loads all sub files into the maps. Ha.
	 * @throws IOException shut the fuck up you stupid shit
	 */
	private void load() throws IOException {
		try (
				GZIPInputStream gzipInputStream = new GZIPInputStream(ResourceLoader.class.getResourceAsStream("/ws"));
				InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
			String file = bufferedReader.readLine();
			file = new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(file), "UTF-8");
			String currentFile = "";
			String currentPage = "";
			String currentImage = "";
			String line;
			BufferedReader lineReader = new BufferedReader(new StringReader(file));
			while ((line = lineReader.readLine()) != null) {
				
				if(line.isEmpty()) {
					continue;
				}
				
				if(line.startsWith("file:")) {
					currentFile = line.split("file:")[1];
				}
				
				if(currentFile.contentEquals("html")) {
					if(line.startsWith("site:")) {
						if(pages.get(line.split("site:")[1]) == null) {
							currentPage = line.split("site:")[1];
							continue;
						}
					}
					
					pages.put(currentPage, new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(line), "UTF-8")+"\n");
				} else if(currentFile.contentEquals("images")) {
					if(line.startsWith("image:")) {
						currentImage = line.split("image:")[1];
						continue;
					}
					images.put(currentImage, javax.xml.bind.DatatypeConverter.parseBase64Binary(line));
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Information to display. My words can expand and be discovered. At a distance.
	 * @author not Oikmo
	 */
	private static class ServerHttpHandler implements HttpHandler {
		/** Path to the given page */
		private String filePath;
		private String endpoint;
		
		/**
		 * Instantiates handler with page.
		 * @param filePath Path to the subfile.
		 */
		public ServerHttpHandler(String filePath, String endpoint) {
			this.filePath = filePath;
			this.endpoint = endpoint;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			boolean error404 = false;
			if(!exchange.getRequestURI().toString().contentEquals(endpoint)) {
				error404 = true;
			}

			String directory = error404 ? "404" : this.filePath;
			String response = "";
			Scanner scanner = new Scanner(pages.get(directory));

			try {
				String line;
				while((line = scanner.nextLine()) != null) {
					response += line;
				}
				scanner.close();
			} catch(Exception e) {}

			exchange.sendResponseHeaders(error404 ? 404 : 200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
	
	/**
	 * Visual stimuli. Helps to get points across. Maybe. If they aren't stupid. In which you are.
	 * @author not Oikmo
	 */
	private static class ServerImageHttpHandler implements HttpHandler {
		/** Path to the given image */
		private String filePath;
		
		/**
		 * Instantiates handler with image path.
		 * @param filePath Path to the subfile.
		 */
		public ServerImageHttpHandler(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(images.get(filePath));
			exchange.sendResponseHeaders(200, baos.toByteArray().length);
			OutputStream os = exchange.getResponseBody();
			os.write(baos.toByteArray());
			os.close();
		}
	}
}
