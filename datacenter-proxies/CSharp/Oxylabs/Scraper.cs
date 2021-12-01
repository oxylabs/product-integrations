using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace Oxylabs
{
    class Scraper
    {
        private readonly ApiClient apiClient;
        private readonly FileManager fileManager;
        private readonly ConsoleWriter consoleWriter;

        public Scraper(
            ApiClient apiClient,
            FileManager fileManager,
            ConsoleWriter consoleWriter
    )
        {
            this.apiClient = apiClient;
            this.fileManager = fileManager;
            this.consoleWriter = consoleWriter;
        }

        public void Scrape(int position, String proxy, String url)
        {
            int retry = 0;

            do
            {
                HttpResponseMessage response = null;
                try
                {
                    response = this.apiClient.FetchPage(proxy, url);
                }
                catch (Exception e)
                {
                    this.fileManager.WriteError(String.Format("{0} - Response {1}", url, e.Message));
                    this.consoleWriter.WritelnError(String.Format("{0} - Response {1}", url, e.Message));
                }

                if (response != null && !response.IsSuccessStatusCode)
                {
                    this.fileManager.WriteError(String.Format("{0} - Response code {1}", url, response.StatusCode.ToString()));
                    this.consoleWriter.WritelnError(String.Format("{0} - Response code {1}", url, response.StatusCode.ToString()));
                }

                if (response != null && response.IsSuccessStatusCode)
                {
                    try
                    {
                        var body = response.Content.ReadAsStringAsync().Result;
                        this.fileManager.writeSuccess(position, body);
                    }
                    catch (Exception e)
                    {
                        this.consoleWriter.WritelnError(String.Format("{0} - Error when parsing response: {0}", url, e.Message));
                    }
                    break;
                }

                retry += 1;
                if (retry >= Settings.RETRIES_NUM)
                {
                    break;
                }
            } while (true);

        }
    }
}
