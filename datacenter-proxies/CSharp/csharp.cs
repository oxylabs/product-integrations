using System;
using System.Net;

class Example
{
    static void Main()
    {
        var client = new WebClient();
        client.Proxy = new WebProxy("PROXY:PORT");
        client.Proxy.Credentials = new NetworkCredential("username", "pass");
        Console.WriteLine(client.DownloadString("https://ip.oxylabs.io"));
    }
}