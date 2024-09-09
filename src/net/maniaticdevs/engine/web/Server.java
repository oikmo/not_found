package net.maniaticdevs.engine.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	
	static List<HttpContext> contexts = new ArrayList<>();
	
	// Main Method
	public static void main(String[] args) throws IOException 
	{
		// Create an HttpServer instance
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

		// Create a context for a specific path and set the handler
		contexts.add(server.createContext("/", new ServerHttpHandler("main")));
		contexts.add(server.createContext("/test", new ServerHttpHandler("test")));
		
		// Start the server
		server.setExecutor(null); // Use the default executor
		server.start();

		System.out.println("Server is running on port 8000");
	}

	// define a custom HttpHandler
	static class ServerHttpHandler implements HttpHandler {
		
		private String directory;
		
		public ServerHttpHandler(String directory) {
			this.directory = directory;
		}
		
		@Override
		public void handle(HttpExchange exchange) throws IOException 
		{
			boolean 404uyeah = false;
			if(directory.contentEquals("main")) {
				if(!exchange.getRequestURI().toString().contentEquals("/")) {
					
				}
			}
			// handle the request
			String response = "";
			Scanner scanner = new Scanner(Server.class.getResourceAsStream("/html/"+directory+".html"));
			
			
			System.out.println();
			try {
				String line;
				while((line = scanner.nextLine()) != null) {
					response += line;
				}
				scanner.close();
			} catch(Exception e) {}
			
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}
