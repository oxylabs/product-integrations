using System;
using System.Collections.Generic;
using System.Text;

namespace Oxylabs
{
    class Proxy
    {
        private String url;
        private String proxyHost;
        private String proxyUsername;
        private String proxyPassword;

        public Proxy(String url,
                     String proxyHost,
                     String proxyUsername,
                     String proxyPassword)
        {
            this.url = url;
            this.proxyHost = proxyHost;
            this.proxyUsername = proxyUsername;
            this.proxyPassword = proxyPassword;
        }

        
        public String GetProxyHost()
        {
            return proxyHost;
        }

        public String GetProxyUsername()
        {
            return proxyUsername;
        }

        public String GetProxyPassword()
        {
            return proxyPassword;
        }

        public String GetUrl()
        {
            return url;
        }

        public static Proxy CreateProxyByUrl(Dictionary<string, string> proxyMap, String url)
        {
            var country = "";
            var urlParts = url.Split(";");
            if (urlParts.Length == 2)
            {
                url = urlParts[0];
                country = urlParts[1];
            }

            var proxyAddress = proxyMap[Settings.DEFAULT_PROXY_INDEX_NAME];
            if (proxyMap.ContainsKey(country))
            {
                proxyAddress = proxyMap[country];
            }

            var proxyUsername = String.Format("customer-{0}", Settings.USERNAME);

            return new Proxy(url, proxyAddress, proxyUsername, Settings.PASSWORD);
        }
    }
}
