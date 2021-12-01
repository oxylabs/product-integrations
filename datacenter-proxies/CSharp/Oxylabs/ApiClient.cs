using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;

namespace Oxylabs
{
    class ApiClient
    {
        private readonly static String API_URL = "https://proxy.oxylabs.io/all";

        private ConsoleWriter consoleWriter;
        private HeaderGenerator headerGenerator;

        public ApiClient(
                ConsoleWriter consoleWriter,
                HeaderGenerator headerGenerator
        )
        {
            this.consoleWriter = consoleWriter;
            this.headerGenerator = headerGenerator;
        }

        public String[] GetProxyList()
        {
            HttpClient client = new HttpClient();
            Uri baseUri = new Uri(API_URL);
            client.BaseAddress = baseUri;
            client.DefaultRequestHeaders.Clear();
            client.DefaultRequestHeaders.ConnectionClose = true;

            var values = new List<KeyValuePair<string, string>>();
            values.Add(new KeyValuePair<string, string>("grant_type", "client_credentials"));
            var content = new FormUrlEncodedContent(values);

            var authenticationString = $"{Settings.USERNAME}:{Settings.PASSWORD}";
            var base64EncodedAuthenticationString = Convert.ToBase64String(System.Text.ASCIIEncoding.ASCII.GetBytes(authenticationString));

            var requestMessage = new HttpRequestMessage(HttpMethod.Get, "");
            requestMessage.Headers.Authorization = new AuthenticationHeaderValue("Basic", base64EncodedAuthenticationString);
            requestMessage.Content = content;

            var task = client.SendAsync(requestMessage);
            var response = task.Result;
            response.EnsureSuccessStatusCode();
            string responseBody = response.Content.ReadAsStringAsync().Result;

            return responseBody.Split("\n");
        }

        internal HttpResponseMessage FetchPage(string proxy, string url)
        {
            var webProxy = new WebProxy
            {
                Address = new Uri($"http://{proxy}"),
                BypassProxyOnLocal = false,
                UseDefaultCredentials = false,

                Credentials = new NetworkCredential(
                userName: Settings.USERNAME,
                password: Settings.PASSWORD
                )
            };

            var httpClientHandler = new HttpClientHandler
            {
                Proxy = webProxy,
            };


            var client = new HttpClient(handler: httpClientHandler, disposeHandler: true);

            Uri baseUri = new Uri(url);
            client.BaseAddress = baseUri;
            client.DefaultRequestHeaders.Clear();
            client.DefaultRequestHeaders.ConnectionClose = true;

            var requestMessage = new HttpRequestMessage(HttpMethod.Get, "");

            var generatedHeaders = this.headerGenerator.Generate();
            foreach (var headerKey in generatedHeaders.Keys)
            {
                requestMessage.Headers.TryAddWithoutValidation(headerKey, generatedHeaders[headerKey]);
            }

            var task = client.SendAsync(requestMessage);
            
            return task.Result;
        }
    }
}