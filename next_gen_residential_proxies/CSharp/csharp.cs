//Full Next-gen residential proxies documentation: https://docs.oxylabs.io/next-gen-residential-proxies/index.html#introduction

using System;
using System.Net;

class Example
{
    static void Main()
    {   
        ServicePointManager.ServerCertificateValidationCallback = delegate { return true; }; //It is required to ignore certificate

        var client = new WebClient();
        client.Proxy = new WebProxy("ngrp.oxylabs.io:60000");
        client.Proxy.Credentials = new NetworkCredential("USERNAME", "PASSWORD");

        client.Headers.Add("X-Oxylabs-Session-Id", "123randomString"); //Header for session control
        client.Headers.Add("X-Oxylabs-Geo-Location", "Germany"); //Header to choose proxy location. Full country list: https://docs.oxylabs.io/resources/universal-supported-geo_location-values.csv
        client.Headers.Add("Your-Custom-Header", "Custom header value"); //Custom headers will be forwarded to the website
        client.Headers.Add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/73.0.3683.86 Chrome/73.0.3683.86 Safari/537.36"); //User-Angents can specified by the user. If no user-agents will be specified, system will send one automatically
        client.Headers.Add("Cookie", "NID=1234567890; 1P_JAR=0987654321"); //Cookie headers
        client.Headers.Add("X-Oxylabs-Status-Code", "500,501,502,503"); //Specify response codes that would not trigger auto-retry if target returns custom codes
        client.Headers.Add("X-Oxylabs-Render": "html"); //Website Javascript will be rendered. Change to "png" in order to receive a screenshot of rendered page.

        Console.WriteLine(client.DownloadString("https://ip.oxylabs.io"));
    }
}
