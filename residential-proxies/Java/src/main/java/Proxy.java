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

    public static Proxy createProxyByUrl(String url) {
        var urlParts = url.split(";");
        if (urlParts.length == 2) {
            var proxyUsername = String.format("customer-%s-cc-%s", Settings.USERNAME, urlParts[1]);

            return new Proxy(urlParts[0], Settings.PROXY_ADDRESS, proxyUsername, Settings.PASSWORD);
        }

        var proxyUsername = String.format("customer-%s", Settings.USERNAME);

        return new Proxy(url, Settings.PROXY_ADDRESS, proxyUsername, Settings.PASSWORD);
    }
}
