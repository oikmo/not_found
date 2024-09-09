package net.maniaticdevs.engine.web;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.api.ResourceLoader;

/**
 * Runs 
 * @author Oikmo
 */
public class WebServer implements Runnable {

	private static Map<String, String> pages = new HashMap<>();
	private static Map<String, BufferedImage> pngs = new HashMap<>();
	private static Map<String, byte[]> gifs = new HashMap<>();

	public static void main(String[] args)  {
		new Thread(new WebServer()).run();
	}

	public WebServer() {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext("/", new ServerHttpHandler("index"));
			server.createContext("/test.html", new ServerHttpHandler("test"));
			
			server.createContext("/smile.png", new ServerImageHttpHandler("smile.png"));
			server.createContext("/favicon.png", new ServerImageHttpHandler("favicon.png"));
			server.createContext("/matrix.gif", new ServerImageHttpHandler("matrix.gif"));
			server.setExecutor(null);
			server.start();

			System.out.println("Website is running on port 8000");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
					if(line.startsWith("png:")) {
						currentImage = line.split("png:")[1]+".png";
						continue;
					}
					if(line.startsWith("gif:")) {
						currentImage = line.split("gif:")[1]+".gif";
						continue;
					}
					if(currentImage.contains("png")) {
						pngs.put(currentImage, ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(line))));
					} else if(currentImage.contains("gif")) {
						gifs.put(currentImage, javax.xml.bind.DatatypeConverter.parseBase64Binary(line));
					}
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static class ServerHttpHandler implements HttpHandler {

		private String filePath;

		public ServerHttpHandler(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			boolean error404 = false;
			if(filePath.contentEquals("index") && !exchange.getRequestURI().toString().contentEquals("/")) {
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

	private static class ServerImageHttpHandler implements HttpHandler {

		private String filePath;

		public ServerImageHttpHandler(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if(filePath.substring(filePath.length()-3).contentEquals("png")) {
				ImageIO.write(pngs.get(filePath), filePath.substring(filePath.length()-3), baos);
			} else if(filePath.substring(filePath.length()-3).contentEquals("gif")) {
				baos.write(gifs.get(filePath));
			}
			
			exchange.sendResponseHeaders(200, baos.toByteArray().length);
			OutputStream os = exchange.getResponseBody();
			os.write(baos.toByteArray());
			os.close();
		}
	}


}
