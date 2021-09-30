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
            string requestUrl = "https://data.oxylabs.io/v1/queries";
            var request = WebRequest.Create(requestUrl);
            request.Method = "POST";
            request.ContentType = "application/json";
            request.Headers.Add("Authorization", $"Basic {EncodedAuth("user", "pass1")}");

            var payload = new
            {
                source = "universal",
                url = "http://ip.oxylabs.io",
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
                Console.WriteLine($"Query error!");
                PrintResponse(ex.Response);
            }
        }

        private void PrintResponse(WebResponse response)
        {
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
