using System;
using System.IO;

namespace Oxylabs
{
    class FileManager
    {
        public static String FAILED_REQUESTS_TXT = "failed_requests.txt";

        private ConsoleWriter consoleWriter;

        public FileManager(ConsoleWriter consoleWriter)
        {
            this.consoleWriter = consoleWriter;

            File.Create(FAILED_REQUESTS_TXT).Dispose();
        }

        public string[] ReadUrlList()
        {
            this.consoleWriter.Writeln("Reading from the list...");

            try
            {
                return System.IO.File.ReadAllLines(Settings.URL_LIST_NAME);
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

        public void WriteSuccess(int position, String contents)
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
