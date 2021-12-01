using System;
using System.Collections.Generic;
using System.Text;

namespace Oxylabs
{
    class Settings
    {
        public readonly static String USERNAME = "";
        public readonly static String PASSWORD = "";
        public readonly static int TIMEOUT = 5;
        public readonly static int REQUESTS_RATE = 10;
        public readonly static int RETRIES_NUM = 3;
        public readonly static String URL_LIST_NAME = "./files/url_list_residential.txt";
        public readonly static String BROWSER_LIST_NAME = "./files/browser_headers.json";
        public readonly static String AGENT_LIST_NAME = "./files/user_agents.json";
        public readonly static String PROXY_ADDRESS = "ngrp.oxylabs.io:60000";
    }
}
