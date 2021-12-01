import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String API_URL = "https://proxy.oxylabs.io/all";
    private static final String PROXY_AUTHORIZATION_HEADER = "Proxy-Authorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final ConsoleWriter consoleWriter;
    private final HeaderGenerator headerGenerator;

    private final OkHttpClient simpleClient;

    public ApiClient(
            ConsoleWriter consoleWriter,
            HeaderGenerator headerGenerator
    ) {
        this.consoleWriter = consoleWriter;
        this.headerGenerator = headerGenerator;
        this.simpleClient = this.createSimpleClient();
    }

    public String[] getProxyList() {
        this.consoleWriter.writeln("Retrieving proxy list...");
        var request = new Request
                .Builder()
                .url(API_URL)
                .build();

        try {
            var response = this.simpleClient.newCall(request).execute();
            if (response.code() != 200) {
                this.consoleWriter.writelnAndExit("Failed to fetch proxy list, status code " + response.code());
            }

            assert response.body() != null;
            var body = response.body().string();

            return body.split("\n");
        } catch (Exception e) {
            this.consoleWriter.writelnAndExit("Failed to write to file: " + e.getMessage());
            return null;
        }
    }

    private OkHttpClient createSimpleClient() {
        Authenticator authenticator = (route, response) -> {
            String credential = Credentials.basic(Settings.USERNAME, Settings.PASSWORD);

            return response
                    .request()
                    .newBuilder()
                    .header(AUTHORIZATION_HEADER, credential)
                    .build();
        };

        return new OkHttpClient
                .Builder()
                .addInterceptor(new RateLimitInterceptor())
                .authenticator(authenticator)
                .connectTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public Response fetchPage(String proxy, String url) throws IOException {
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(Settings.USERNAME, Settings.PASSWORD);
            return response.request().newBuilder()
                    .header(PROXY_AUTHORIZATION_HEADER, credential)
                    .build();
        };

        URI proxyUri;
        try {
            proxyUri = new URI("http://" + proxy);
        } catch (URISyntaxException e) {
            this.consoleWriter.writelnAndExit("Failed to parse proxy URI: " + e.getMessage());
            return null;
        }

        var okHttpProxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(proxyUri.getHost(), proxyUri.getPort())
        );
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new RateLimitInterceptor())
                .proxyAuthenticator(proxyAuthenticator)
                .connectTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .proxy(okHttpProxy)
                .build();

        var request = new Request
                .Builder()
                .url(url)
                .headers(this.buildHeaders())
                .build();

        return client.newCall(request).execute();
    }

    public Headers buildHeaders() {
        var builder = new Headers.Builder();
        var randomHeaders = this.headerGenerator.generate();
        var randomHeadersKeys = randomHeaders.keySet();

        for (var headerKey : randomHeadersKeys) {
            var headerValue = randomHeaders.get(headerKey).getAsString();
            builder = builder.add(headerKey, headerValue);
        }

        return builder.build();
    }
}
