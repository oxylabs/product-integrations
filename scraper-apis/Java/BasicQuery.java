import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class BasicQuery {
	private static final String AUTH = "user:pass";

	public static void main(String[] args) throws IOException{
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(AUTH.getBytes());

		URL url = new URL ("https://data.oxylabs.io/v1/queries");
		
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		
		con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        
        con.setRequestProperty("Authorization", authHeaderValue);
		
		con.setDoOutput(true);
		
		//JSON String need to be constructed for the specific resource. 
		//We may construct complex JSON using any third-party JSON libraries such as jackson or org.json
		String payload = "{\"source\": \"universal\", \"url\": \"http://ip.oxylabs.io\"}";
		
		try(OutputStream os = con.getOutputStream()){
			byte[] input = payload.getBytes("utf-8");
			os.write(input, 0, input.length);			
		}

		int code = con.getResponseCode();
		System.out.println(code);
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());
		}
    }
}