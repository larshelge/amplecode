using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace IPExport
{
    /// <summary>
    ///  This is the main class responsible for driving the export process.
    /// </summary>
    class ExportService
    {
        SvxProvider provider = new SvxProvider();
        SvxMarshaller marshaller = new SvxMarshaller();
        Uploader uploader = new Uploader();

        public int export()
        {
            string serverUser = "interplay";
            string serverPwd = "ip4321";
            string appUser = "interplay@interplay.com";
            string appPwd = "interplay";

            // Check network connection
            
            bool networkIsAvailable = uploader.networkIsAvailable();

            if (!networkIsAvailable)
            {
                return ExportConstants.RESULT_NETWORK_UNAVAILABLE;
            }

            // Check server connection

            bool serverIsAvailable = !uploader.serverIsAvailable();

            if (!serverIsAvailable)
            {
                return ExportConstants.RESULT_UPLOAD_SERVER_UNAVAILABLE;
            }
            
            // Upload video files to server

            bool result = uploader.upload(serverUser, serverPwd); // TODO upload files with clips only

            // Get SVX model from database

            Svx svx = provider.provide();

            MemoryStream stream = new MemoryStream();

            // Convert SVX model into XML and write to stream

            marshaller.marshal(svx, stream);

            // Send XML stream as request to server

            string status = uploader.request(stream.ToArray(), appUser, appPwd);

            return ExportConstants.RESULT_SUCCESS;
        }
    }
}
