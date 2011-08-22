using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace IPExport
{
    class ExportUtils
    {
        public static String getExecutableDir()
        {
            String executableDir = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);

            return executableDir + "\\";
        }
    }
}
