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

        internal HttpResponseMessage FetchPage(Proxy proxy, string url)
        {
            var webProxy = new WebProxy
            {
                Address = new Uri($"http://{proxy.GetProxyHost()}"),
                BypassProxyOnLocal = false,
                UseDefaultCredentials = false,

                Credentials = new NetworkCredential(
                userName: proxy.GetProxyUsername(),
                password: proxy.GetProxyPassword()
                )
            };

            var httpClientHandler = new HttpClientHandler
            {
                Proxy = webProxy,
                AutomaticDecompression = DecompressionMethods.GZip | DecompressionMethods.Deflate,
            };

            httpClientHandler.ServerCertificateCustomValidationCallback = (message, cert, chain, sslPolicyErrors) =>
            {
                return true;
            };


            var client = new HttpClient(handler: httpClientHandler, disposeHandler: true);


            Uri baseUri = new Uri(url);
            client.BaseAddress = baseUri;

            var requestMessage = new HttpRequestMessage(HttpMethod.Get, "");

            var generatedHeaders = this.headerGenerator.Generate(proxy.GetCountry());
            foreach (var headerKey in generatedHeaders.Keys)
            {
                requestMessage.Headers.TryAddWithoutValidation(headerKey, generatedHeaders[headerKey]);
            }


            var task = client.SendAsync(requestMessage);

            return task.Result;
        }
    }
}