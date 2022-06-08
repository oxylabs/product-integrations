using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;

namespace Oxylabs
{
    class FileManager
    {
        public static String FAILED_REQUESTS_TXT = "failed_requests.txt";
        public static String REGEX_PATTERN = @"^dc\.(?<country>\w{2})-?pr\.oxylabs\.io:\d+$";


        private ConsoleWriter consoleWriter;

        public FileManager(ConsoleWriter consoleWriter)
        {
            this.consoleWriter = consoleWriter;

            File.Create(FAILED_REQUESTS_TXT).Dispose();
        }

        public string[] ReadUrlList()
        {
            return this.ReadLinesFromFile(Settings.URL_LIST_NAME);
        }

        public Dictionary<string, string> ReadProxyMap()
        {
            var proxyList = this.ReadLinesFromFile(Settings.PROXY_LIST_NAME);
            if(proxyList == null)
            {
                throw new Exception("Could not read proxy list from file");
            }

            var proxyMap = new Dictionary<string, string>();

            foreach (var proxyUrl in proxyList)
            {
                var match = Regex.Match(proxyUrl, REGEX_PATTERN);
                if (match.Groups.Count == 2)
                {
                    var country = match.Groups[1].ToString().ToUpper();
                    proxyMap.Add(country, proxyUrl);
                    continue;
                }

                proxyMap.Add(Settings.DEFAULT_PROXY_INDEX_NAME, proxyUrl);
            }

            return proxyMap;

        }

        public string[] ReadLinesFromFile(string fileName)
        {
            this.consoleWriter.Writeln("Reading from the list...");

            try
            {
                return System.IO.File.ReadAllLines(fileName);
            }
            catch (Exception e)
            {
                this.consoleWriter.WritelnAndExit("Failed to read from file: " + e.Message);
                return null;
            }
        }

        public void WriteError(String contents)
        {

            try
            {
                using (var stream = File.Open(FAILED_REQUESTS_TXT, FileMode.Append, FileAccess.Write, FileShare.ReadWrite))
                {
                    using (var fs = new StreamWriter(stream))
                    {
                        fs.WriteLine(contents);
                    }
                }
            }
            catch (Exception e)
            {
                this.consoleWriter.WritelnError("Failed to write to file: " + e.Message);
            }
        }

        public void writeSuccess(int position, String contents)
        {
            try
            {
                using (var stream = File.Open("result_" + position + ".html", FileMode.Create, FileAccess.Write, FileShare.ReadWrite))
                {
                    using (var fs = new StreamWriter(stream))
                    {
                        fs.WriteLine(contents);
                    }
                }
            }
            catch (Exception e)
            {
                this.consoleWriter.WritelnError("Failed to write to file: " + e.Message);
            }
        }
    }
}
