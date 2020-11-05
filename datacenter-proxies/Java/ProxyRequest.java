import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ProxyRequest 
{
    public static void main( String[] args ) {
        String target = "https://ip.oxylabs.io";
        String proxyHost = "PROXY_HOST_OR_IP";
        int proxyPort = 60000;
        String proxyUsername = "USERNAME";
        String proxyPassword = "PASSWORD";

        enableAuthForHttps();
        setAuthenticator(proxyUsername, proxyPassword);

        try {
            HttpURLConnection connection = createConnection(proxyHost, proxyPort, target);

            int statusCode = connection.getResponseCode();
            Map<String,List<String>> headers = connection.getHeaderFields();
            String body = getResponseBody(connection);

            System.out.println("Status Code: " + statusCode);
            System.out.println("Headers: " + headers);
            System.out.println("Body: " + body);
        } catch (IOException err) {
            System.out.println(err);
        }
    }

    private static void enableAuthForHttps() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    }

    private static void setAuthenticator(String username, String password) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType().equals(RequestorType.PROXY)) {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
                return super.getPasswordAuthentication();
            }
        });
    }

    private static HttpURLConnection createConnection(
        String proxyHost,
        int proxyPort,
        String target
    ) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        URL url = new URL(target);
        return (HttpURLConnection)url.openConnection(proxy);
    }

    private static String getResponseBody(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) 
                response.append(currentLine);
    
            return response.toString();
        }
    }
}