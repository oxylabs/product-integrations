using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Oxylabs
{
    class Program
    {
        static void Main(string[] args)
        {
            var watch = System.Diagnostics.Stopwatch.StartNew();

            var consoleWriter = new ConsoleWriter();
            var headerGenerator = new HeaderGenerator(
                    Settings.BROWSER_LIST_NAME,
                    Settings.AGENT_LIST_NAME,
                    consoleWriter
            );

            var fileManager = new FileManager(consoleWriter);
            var apiClient = new ApiClient(consoleWriter, headerGenerator);
            var scraper = new Scraper(apiClient, fileManager, consoleWriter);

            var urlList = fileManager.ReadUrlList();
            var tasks = new List<Task>();

            consoleWriter.Writeln("Gathering results...");
            for (int i = 0; i < urlList.Length; i++)
            {
                var url = urlList[i];
                var proxy = Proxy.CreateProxyByUrl(url);
                var position = i + 1;

                var task = Task.Run(() => scraper.Scrape(position, proxy));
                tasks.Add(task);
            }


            foreach (var task in tasks)
            {
                task.Wait();
            }


            watch.Stop();
            var elapsedSeconds = watch.ElapsedMilliseconds / 1000;

            consoleWriter.Writeln(String.Format("Script finished after {0}s", elapsedSeconds));
        }
    }
}
