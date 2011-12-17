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
        //public static string SERVER_BASE_URL = "fotballab.no"; // Production network
        //public static string SERVER_BASE_URL = "178.79.133.34"; // Test server
        public static string SERVER_BASE_URL = "192.168.0.194"; // Local network

        public static string UPLOAD_URL = "http://" + SERVER_BASE_URL + ":8080/nf/importStream"; // Change
        public static string UPLOAD_TARGET = "interplay@" + SERVER_BASE_URL + ":upload";

        public static string DEBUG_FILENAME = "ipsys_export.xml";
        public static string DB_FILENAME = "ipsys_guest.mdb";

        public static string UPLOAD_CONTENT_TYPE = "application/xml";
        public static string UPLOAD_METHOD = "post";
        public static string XMLNS = "http://fotballab.no/schema";
        public static string XSI = "http://www.w3.org/2001/XMLSchema-instance";
        public static string XSI_LOCATION = "http://fotballab.no/schema http://fotballab.no/schema/svx.xsd";
        public static string DATE_FORMAT = "yyyy-MM-dd";
        public static int FRAMES_PER_SEC = 25;
        public static string SCP_EXE = "pscp.exe";
        public static string VIDEO_DIR = "video\\";
        public static string VIDEO_PATTERN = "*.mp4";

        public static int RESULT_SUCCESS = 0;
        public static int RESULT_NETWORK_UNAVAILABLE = 1;
        public static int RESULT_UPLOAD_SERVER_UNAVAILABLE = 2;

        public static string SERVER_USER = "interplay";
        public static string SERVER_PWD = "ip4321"; // TODO avoid storing passwords in clear text
        public static string APP_USER = "interplay@interplay.com";
        public static string APP_PWD = "interplay";

    }
}
