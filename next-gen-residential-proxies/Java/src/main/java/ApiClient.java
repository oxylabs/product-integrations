import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
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

    public Response fetchPage(Proxy proxy) throws IOException {
        var url = proxy.getUrl();

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

        var client = createHttpClient(proxyAuthenticator, proxyUri);
        var request = createRequest(proxy, url);

        return client.newCall(request).execute();
    }

    @NotNull
    private Request createRequest(Proxy proxy, String url) {
        var request = new Request
                .Builder()
                .url(url)
                .headers(this.buildHeaders(proxy.getCountry()))
                .build();
        return request;
    }

    @NotNull
    private OkHttpClient createHttpClient(Authenticator proxyAuthenticator, URI proxyUri) {
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLSocketFactory sslSocketFactory = null;
        try {
            var sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (Exception e) {
            this.consoleWriter.writelnAndExit("Failed to disable ssl:" + e.getMessage());
        }

        var okHttpProxy = new java.net.Proxy(
                java.net.Proxy.Type.HTTP,
                new InetSocketAddress(proxyUri.getHost(), proxyUri.getPort())
        );
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new RateLimitInterceptor())
                .addInterceptor(new GzipInterceptor())
                .proxyAuthenticator(proxyAuthenticator)
                .connectTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Settings.TIMEOUT, TimeUnit.SECONDS)
                .proxy(okHttpProxy)
                .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();
        return client;
    }

    public Headers buildHeaders(String country) {
        var builder = new Headers.Builder();
        var randomHeaders = this.headerGenerator.generate(country);
        var randomHeadersKeys = randomHeaders.keySet();

        for (var headerKey : randomHeadersKeys) {
            var headerValue = randomHeaders.get(headerKey).getAsString();
            builder = builder.add(headerKey, headerValue);
        }

        return builder.build();
    }
}
