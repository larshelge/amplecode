using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace IPExport
{
    /// <summary>
    ///  This class is responsible for holding centralized constant values.
    /// </summary>
    class ExportConstants
    {
        public static string EXPORT_LOCATION = "d:\\lars\\export\\";
        public static string EXPORT_FILENAME = EXPORT_LOCATION + "ipsys_export.xml";
        public static string DATA_FILENAME = EXPORT_LOCATION + "ipsys_guest.mdb";

        public static string RELIABLE_VERIFICATION_URL = "www.google.com";
        public static string SERVER_VERIFICATION_URL = "amplecode.org"; // Temp
        public static string UPLOAD_URL = "http://192.168.0.194:8080/nf/importStream"; // Temp
        public static string UPLOAD_TARGET = "interplay@login.amplecode.org:upload";
        public static string UPLOAD_CONTENT_TYPE = "application/xml";
        public static string UPLOAD_METHOD = "post";
        public static string XMLNS = "http://www.toppfotball.no/schema";
        public static string XSI = "http://www.w3.org/2001/XMLSchema-instance";
        public static string XSI_LOCATION = "http://www.toppfotball.no/schema svx.xsd";
        public static string DATE_FORMAT = "yyyy-MM-dd";
        public static int FRAMES_PER_SEC = 25;
        public static string SCP_EXE = "pscp.exe";
        public static string VIDEO_PATTERN = "*.mp4";

        public static int RESULT_SUCCESS = 0;
        public static int RESULT_NETWORK_UNAVAILABLE = 1;
        public static int RESULT_UPLOAD_SERVER_UNAVAILABLE = 2;
    }
}
