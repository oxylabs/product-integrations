using System;

namespace Oxylabs
{
    class Settings
    {
        public readonly static String USERNAME = "";
        public readonly static String PASSWORD = "";
        public readonly static int TIMEOUT = 5;
        public readonly static int REQUESTS_RATE = 10;
        public readonly static int RETRIES_NUM = 3;
        public readonly static String URL_LIST_NAME = "./files/url_list_shared_dc.txt";
        public readonly static String PROXY_LIST_NAME = "./files/proxy_list_shared_dc.txt";
        public readonly static String BROWSER_LIST_NAME = "./files/browser_headers.json";
        public readonly static String AGENT_LIST_NAME = "./files/user_agents.json";
        public readonly static String DEFAULT_PROXY_INDEX_NAME = "DEFAULT";
    }
}
