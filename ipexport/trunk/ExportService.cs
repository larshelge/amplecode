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
        private SvxProvider provider = new SvxProvider();
        private SvxMarshaller marshaller = new SvxMarshaller();
        private Uploader uploader = new Uploader();

        public int export()
        {
            Console.WriteLine("Export process started, executable dir: " + ExportUtils.getExecutableDir() + ", server URL: " + ExportConstants.SERVER_BASE_URL);

            // TODO: Check server connection

            // Upload video files to server

            bool result = uploader.upload(ExportConstants.SERVER_USER, ExportConstants.SERVER_PWD); // TODO upload files with clips only

            Console.WriteLine("Uploaded videos to server");

            // Get SVX model from database

            Svx svx = provider.provide();

            Console.WriteLine("Retrieved meta-data");

            // Convert SVX model into XML and write to stream

            MemoryStream stream = new MemoryStream();

            marshaller.marshal(svx, stream);

            Console.WriteLine("Created meta-data exchange stream");

            // Send XML stream as request to server

            string status = uploader.sendSvx(stream.ToArray(), ExportConstants.APP_USER, ExportConstants.APP_PWD);

            Console.WriteLine("Sent meta-data to server - Done");

            return ExportConstants.RESULT_SUCCESS;
        }
    }
}
