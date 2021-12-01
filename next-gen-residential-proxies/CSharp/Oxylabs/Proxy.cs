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
        private String country;

        public Proxy(String url,
                     String proxyHost,
                     String proxyUsername,
                     String proxyPassword,
                     String country)
        {
            this.url = url;
            this.proxyHost = proxyHost;
            this.proxyUsername = proxyUsername;
            this.proxyPassword = proxyPassword;
            this.country = country;
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

        public String GetCountry()
        {
            return country;
        }

        public static Proxy CreateProxyByUrl(String url)
        {
            var parsedUrl = url;
            String country = null;

            var urlParts = url.Split(";");
            if (urlParts.Length == 2)
            {
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
}
