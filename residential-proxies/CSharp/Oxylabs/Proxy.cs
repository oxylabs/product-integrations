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

        public static Proxy CreateProxyByUrl(String url)
        {
            var urlParts = url.Split(";");
            if (urlParts.Length == 2)
            {
                var username = String.Format("customer-{0}-cc-{0}", Settings.USERNAME, urlParts[1]);

                return new Proxy(urlParts[0], Settings.PROXY_ADDRESS, username, Settings.PASSWORD);
            }

            var proxyUsername = String.Format("customer-{0}", Settings.USERNAME);

            return new Proxy(url, Settings.PROXY_ADDRESS, proxyUsername, Settings.PASSWORD);
        }
    }
}
