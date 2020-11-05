//Full Next-gen residential proxies documentation: https://docs.oxylabs.io/next-gen-residential-proxies/index.html#introduction

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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.SecureRandom; 

public class ProxyRequest 
{
    public static void main( String[] args ) {
        String target = "https://ip.oxylabs.io";
        String proxyHost = "ngrp.oxylabs.io";
        int proxyPort = 60000;
        String proxyUsername = "USERNAME";
        String proxyPassword = "PASSWORD";

        enableAuthForHttps();
        setAuthenticator(proxyUsername, proxyPassword);
        trustAllCertificates(); //It is required to ignore certificate

        try {
            HttpURLConnection connection = createConnection(proxyHost, proxyPort, target);

            //connection.setRequestProperty("X-Oxylabs-Session-Id", "123randomString"); //Header for session control
            //connection.setRequestProperty("X-Oxylabs-Geo-Location", "Germany"); //Header to choose proxy location. Full country list: https://docs.oxylabs.io/resources/universal-supported-geo_location-values.csv
            //connection.setRequestProperty("Your-Custom-Header", "Custom header value"); //Custom headers will be forwarded to the website
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/73.0.3683.86 Chrome/73.0.3683.86 Safari/537.36"); //User-Angents can specified by the user. If no user-agents will be specified, system will send one automatically
            //connection.setRequestProperty("Cookie", "NID=1234567890; 1P_JAR=0987654321"); //Cookie headers
            //connection.setRequestProperty("X-Oxylabs-Status-Code", "500,501,502,503"); //Specify response codes that would not trigger auto-retry if target returns custom codes
            //connection.setRequestProperty("X-Oxylabs-Render": "html"); //Website Javascript will be rendered. Change to "png" in order to receive a screenshot of rendered page.

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
    //Used to ignore the certificate
    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
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