using System;
using System.Collections.Generic;
using System.Text;

namespace Oxylabs
{
    class ConsoleWriter
    {
        public void WritelnAndExit(String str)
        {
            this.WritelnError(str);
            System.Environment.Exit(1);
        }

        public void WritelnError(String output)
        {
            this.Writeln("ERROR: " + output);
        }

        public void Writeln(String str)
        {
            Console.WriteLine(str);
        }
    }
}
