public class Proxy {
    private final String url;
    private final String proxyHost;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String country;

    public Proxy(String url,
                 String proxyHost,
                 String proxyUsername,
                 String proxyPassword,
                 String country) {
        this.url = url;
        this.proxyHost = proxyHost;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public static Proxy createProxyByUrl(String url) {
        var parsedUrl = url;
        String country = null;

        var urlParts = url.split(";");
        if (urlParts.length == 2) {
            parsedUrl = urlParts[0];
            country = urlParts[1];
        }

        return new Proxy(
                parsedUrl,
                Settings.PROXY_ADDRESS,
                Settings.USERNAME,
                Settings.PASSWORD,
                country
        );
    }
}
