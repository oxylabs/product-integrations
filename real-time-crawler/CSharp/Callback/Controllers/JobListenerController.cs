using System;
using System.IO;
using System.Net;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

[Route("job_listener")]
public class JobListenerController : Controller
{
    private string _username = "user";
    private string _password = "pass";

    // Define /job_listener endpoint that accepts POST requests.
    [HttpPost]
    public ActionResult JobListener()
    {
        try
        {
            using (var reader = new StreamReader(Request.Body))
            {
                string body = reader.ReadToEnd();
                Console.WriteLine(body);
                var callback = JsonConvert.DeserializeObject<CallbackDTO>(body);
                PrintResults(callback);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Listener error: {ex.Message}");
        }
        return Ok();
    }

    private void PrintResults(CallbackDTO callback)
    {
        foreach (var result in callback.Links)
        {
            if (result.Rel.Equals("results"))
            {
                var request = WebRequest.Create(result.Href);
                request.Method = result.Method;
                request.Headers.Add("Authorization", $"Basic {EncodedAuth()}");

                using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
                using (Stream stream = response.GetResponseStream())
                using (StreamReader reader = new StreamReader(stream))
                {
                    var data = reader.ReadToEnd();
                    Console.WriteLine(data);
                }

                break;
            }
        }
    }

    private string EncodedAuth()
    {
        return Convert.ToBase64String(Encoding.GetEncoding("ISO-8859-1").GetBytes(_username + ":" + _password));
    }
}
