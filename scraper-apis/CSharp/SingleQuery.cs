using System;
using System.IO;
using System.Net;
using System.Text;
using Newtonsoft.Json;

namespace single_query_example
{
    class Program
    {
        public void SingleQuery()
        {
            /* This example will submit a job request to Scraper API.
            The job will deliver parsed product data in JSON for Amazon searches
            from United States geo-location*/
            string requestUrl = "https://data.oxylabs.io/v1/queries";
            var request = WebRequest.Create(requestUrl);
            request.Method = "POST";
            request.ContentType = "application/json";
            request.Headers.Add("Authorization", $"Basic {EncodedAuth("user", "pass1")}"); //Don't forget to fill in user credentials

            var payload = new
            {
                source = "amazon_search",
                query = "kettle",
                geo_location = "10005",
                parse = true,
            };

            using (var streamWriter = new StreamWriter(request.GetRequestStream()))
            {
                string json = JsonConvert.SerializeObject(payload);
                streamWriter.Write(json);
                streamWriter.Flush();
            }

            try
            {
                using (var response = request.GetResponse())
                    PrintResponse(response);
            }
            catch (WebException ex)
            {
                Console.WriteLine("Query error!");
                PrintResponse(ex.Response);
            }
        }

        private void PrintResponse(WebResponse response)
        {
            //To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.cs file
            using (Stream stream = response.GetResponseStream())
            using (StreamReader reader = new StreamReader(stream))
            {
                var data = reader.ReadToEnd();
                Console.WriteLine(data);
            }
        }

        private string EncodedAuth(string username, string password)
        {
            return Convert.ToBase64String(Encoding.GetEncoding("ISO-8859-1").GetBytes(username + ":" + password));
        }

        static void Main(string[] args)
        {
            var p = new Program();
            p.SingleQuery();
        }
    }
}
