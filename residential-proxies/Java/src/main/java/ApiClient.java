import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String PROXY_AUTHORIZATION_HEADER = "Proxy-Authorization";

    private final ConsoleWriter consoleWriter;
    private final HeaderGenerator headerGenerator;

    public ApiClient(
            ConsoleWriter consoleWriter,
            HeaderGenerator headerGenerator
    ) {
        this.consoleWriter = consoleWriter;
        this.headerGenerator = headerGenerator;
    }

    public Response fetchPage(Proxy proxy, String url) throws IOException {
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(proxy.getProxyUsername(), proxy.getProxyPassword());
            return response.request().newBuilder()
                    .header(PROXY_AUTHORIZATION_HEADER, credential)
                    .build();
        };

        URI proxyUri;
        try {
            proxyUri = new URI("http://" + proxy.getProxyHost());
        } catch (URISyntaxException e) {
            this.consoleWriter.writelnAndExit("Failed to parse proxy URI: " + e.getMessage());
            return null;
        }

        var okHttpProxy = new java.net.Proxy(
                java.net.Proxy.Type.HTTP,
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
