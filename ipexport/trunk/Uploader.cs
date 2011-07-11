using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.IO;

namespace IPExport
{
    /// <summary>
    ///  This class is responsible for communicating with the remote server and perform operations such as uploading
    ///  video files and posting requests with upload meta-data.
    /// </summary>
    class Uploader
    {
        public void upload(string username, string password)
        {
            Process upload = null;
            string args = "-pw " + password + " " + ExportConstants.EXPORT_LOCATION + ExportConstants.VIDEO_PATTERN + " " + ExportConstants.UPLOAD_TARGET;

            try
            {
                upload = Process.Start(ExportConstants.SCP_EXE, args);
                upload.WaitForExit();
            }
            catch (Exception ex)
            {
                if (upload != null)
                {
                    upload.Close();
                }

                throw new Exception("Failed to upload", ex);
            }
        }

        public string request(byte[] bytes, string username, string password)
        {
            string authHeaderValue = "Basic " + base64Encode(username + ":" + password);

            WebRequest request = WebRequest.Create(ExportConstants.UPLOAD_URL);
            request.ContentType = ExportConstants.UPLOAD_CONTENT_TYPE;
            request.Method = ExportConstants.UPLOAD_METHOD;
            request.ContentLength = bytes.Length;
            request.Headers.Add("Authorization", authHeaderValue); // Requires user to be present on server
            Stream stream = request.GetRequestStream();
            stream.Write(bytes, 0, bytes.Length);
            stream.Close();
            HttpWebResponse response = (HttpWebResponse) request.GetResponse();
            return response.StatusDescription;
        }

        public bool networkIsAvailable()
        {
            return isAvailable(ExportConstants.RELIABLE_VERIFICATION_URL);
        }

        public bool serverIsAvailable()
        {
            return isAvailable(ExportConstants.SERVER_VERIFICATION_URL);
        }

        private bool isAvailable(string url)
        {
            try
            {
                TcpClient client = new TcpClient(url, 80);
                client.Close();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        private string base64Encode(string value)
        {
            byte[] bytes = Encoding.UTF8.GetBytes(value);
            return Convert.ToBase64String(bytes);
        }
    }
}
