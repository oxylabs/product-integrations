import java.util.HashMap;

public class Proxy {
    private final String url;
    private final String proxyHost;
    private final String proxyUsername;
    private final String proxyPassword;

    public Proxy(String url,
                 String proxyHost,
                 String proxyUsername,
                 String proxyPassword) {
        this.url = url;
        this.proxyHost = proxyHost;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getUrl() {
        return url;
    }

    public static Proxy createProxyByUrl(HashMap<String, String> proxyMap, String url) {
        String country = null;

        var urlParts = url.split(";");
        if (urlParts.length == 2) {
            url = urlParts[0];
            country = urlParts[1];
        }

        var proxyAddress = proxyMap.get(Settings.DEFAULT_PROXY_INDEX_NAME);
        if (proxyMap.containsKey(country)) {
            proxyAddress = proxyMap.get(country);
        }

        var proxyUsername = String.format("customer-%s", Settings.USERNAME);

        return new Proxy(url, proxyAddress, proxyUsername, Settings.PASSWORD);
    }
}
