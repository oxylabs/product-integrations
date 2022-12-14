import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class RetrieveJobContent {
	private static final String AUTH = "user:pass";

	public static void main(String[] args) throws IOException{
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(AUTH.getBytes());

		URL url = new URL ("https://data.oxylabs.io/v1/queries/12345678900987654321/results");
		
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		
        con.setRequestProperty("Authorization", authHeaderValue);

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