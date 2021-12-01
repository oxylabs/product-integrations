using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace Oxylabs
{
    class HeaderGenerator
    {
        private String browserHeadersFilename;
        private String userAgentsFilename;
        private ConsoleWriter consoleWriter;
        private Random rand;

        public HeaderGenerator(
                String browserHeadersFilename,
                String userAgentsFilename,
                ConsoleWriter consoleWriter
        )
        {
            this.browserHeadersFilename = browserHeadersFilename;
            this.userAgentsFilename = userAgentsFilename;
            this.consoleWriter = consoleWriter;
            this.rand = new Random();
        }

        public JObject LoadJson(String fileName)
        {
            return JObject.Parse(File.ReadAllText(fileName));
        }

        public Dictionary<String, String> Generate(String country)
        {
            var rand = new Random();

            var browserHeaders = this.LoadJson(this.browserHeadersFilename);
            var randomBrowserName = getRandomKey(browserHeaders);

            var userAgents = this.LoadJson(this.userAgentsFilename);
            var browserUserAgents = userAgents[randomBrowserName].ToObject<string[]>();
            var randomUserAgent = browserUserAgents[rand.Next(0, browserUserAgents.Length - 1)];

            var randomBrowser = browserHeaders[randomBrowserName].ToObject<Dictionary<String, String>>();
            randomBrowser.Add("User-Agent", randomUserAgent);

            if (country != null)
            {
                randomBrowser.Add("X-Oxylabs-Geo-Location", country);
            }

            return randomBrowser;
        }

        public String getRandomKey(JObject obj)
        {
            var randomIndex = this.rand.Next(0, obj.Count - 1);

            int i = 0;
            foreach (var item in obj)
            {
                if (i == randomIndex)
                {
                    return item.Key;
                }

                i++;
            }

            throw new Exception("Could not generate random key");
        }
    }

}
