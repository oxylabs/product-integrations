import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.util.Base64;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.Json;
import javax.json.JsonArray;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Callback {
	private static final String AUTH = "nedasurtc:krG76922Mq";

	public static void main(String[] args) {
		try {
        		HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
			ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10); // depends on server capacity
        		server.createContext("/job_listener", new ListenerHandler());
        		server.setExecutor(threadPoolExecutor); 
        		server.start();
		} catch (Exception e) {
			// Appropriate error handling
			System.out.println(e.getMessage());
		}
    	}

	static class ListenerHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			if (!"POST".equals(httpExchange.getRequestMethod())) {
				// Return method not allowed
				httpExchange.getResponseHeaders().set("Allow", "POST");
				httpExchange.sendResponseHeaders(405, -1);
				return;
			}

			InputStream requestStream = httpExchange.getRequestBody();
			JsonReader requestReader = Json.createReader(requestStream);
			JsonObject requestJson = requestReader.readObject();
			JsonArray links = requestJson.getJsonArray("_links");


			for (JsonValue jvalue : links) {
				JsonObject link = (JsonObject)jvalue;
				if (link.getString("rel").equals("results")) {
					fetchResults(link.getString("href"));
				}
			}

			// Send HTTP 200 response
			httpExchange.sendResponseHeaders(200, -1);
		}

		private void fetchResults(String href) {
			HttpURLConnection con = null;
			String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(AUTH.getBytes());

			try {
				URL url = new URL(href);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Authorization", authHeaderValue);
				con.setRequestMethod("GET");

				InputStream responseStream = con.getInputStream();
				JsonReader responseReader = Json.createReader(responseStream);
				JsonObject responseJson = responseReader.readObject();
				
				// Format the response json as you wish
				System.out.println(responseJson);
			} catch (Exception e) {
				// Appropriate error handling
				System.out.println(e.getMessage());
			} finally {
				if (con != null) {
      					con.disconnect();
    				}
			}
				
		}
	}
}
