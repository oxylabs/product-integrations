public class Settings {
    static final String USERNAME = "";
    static final String PASSWORD = "";
    static final int TIMEOUT = 5;
    static final int REQUESTS_RATE = 10;
    static final int RETRIES_NUM = 3;
    static final String URL_LIST_NAME = "./src/main/resources/url_list_shared_dc.txt";
    static final String PROXY_LIST_NAME = "./src/main/resources/proxy_list_shared_dc.txt";
    static final String BROWSER_LIST_NAME = "./src/main/resources/browser_headers.json";
    static final String AGENT_LIST_NAME = "./src/main/resources/user_agents.json";
    static final String PROXY_REGEX = "^dc\\.(?<country>\\w{2})-?pr\\.oxylabs\\.io:\\d+$";
    static final String DEFAULT_PROXY_INDEX_NAME = "DEFAULT";
}
