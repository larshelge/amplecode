using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.OleDb;

namespace IPExport
{
    /// <summary>
    ///  This is a wrapper class for an OleDbCommand instance. It is an abstraction over connection and data reader.
    /// </summary>
    class CommandWrapper
    {
        private static string connectionString = "Provider=Microsoft.Jet.OLEDB.4.0;Data Source=" + ExportConstants.DATA_FILENAME;
        private OleDbConnection connection;
        private OleDbDataReader reader;

        public CommandWrapper()
        {            
        }

        public CommandWrapper init()
        {
            connection = new OleDbConnection(connectionString);

            try
            {
                connection.Open();
            }
            catch (InvalidOperationException ex)
            {                
                close();
                throw new Exception("Failed to open connection", ex);
            }

            return this;
        }
        
        public OleDbDataReader getReader(string sql)
        {
            try
            {
                OleDbCommand command = new OleDbCommand(sql);
                command.Connection = connection;
                reader = command.ExecuteReader();
                return reader;
            }
            catch (InvalidOperationException ex)
            {
                close();
                throw new Exception("Failed to execute reader", ex);
            }
        }

        public void close()
        {
            if (reader != null)
            {
                try
                {
                    reader.Close();
                }
                catch (Exception)
                {
                }
            }

            if (connection != null)
            {
                try
                {
                    connection.Close();
                }
                catch (Exception)
                {
                }
            }
        }
    }
}
