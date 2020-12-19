using System;
using System.Data;
using System.Configuration;
using System.Data.SqlClient;
using System.IO;
using System.Diagnostics;


namespace Gra_Państwa_Miasta.ClientServer
{
    class DatabaseHandling
    {
        string connectionString;
        SqlConnection connection;

        public DatabaseHandling()
        {
            connectionString = ConfigurationManager.ConnectionStrings["Gra_Państwa_Miasta.Properties.Settings.DatabaseConnectionString"].ConnectionString;
        }

        public bool isInDatabase(string table, string word)
        {
            String query = "SELECT * FROM " + table + " WHERE Nazwa = '" + word + "'";
            using (connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(query, connection))
            using (SqlDataAdapter adapter = new SqlDataAdapter(command))
            {
                DataTable dataTable = new DataTable();
                int count = adapter.Fill(dataTable);
                return (count > 0) ? true : false;
            }
        }

        private void clearTable(string table)
        {
            String query = "TRUNCATE TABLE "+table;
            using (connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(query, connection))
            {
                connection.Open();
                command.ExecuteScalar();
            }
        }


        private void insertQuery(string table, string value)
        {
            String query = "INSERT INTO " + table + " VALUES (@Nazwa)";
            using (connection = new SqlConnection(connectionString))
            using (SqlCommand command = new SqlCommand(query, connection))
            {
                connection.Open();
                command.Parameters.AddWithValue("@Nazwa", value);
                command.ExecuteScalar();
            }
        }

        private void fileToDatabase(string fileName, string table)
        {
            String line;
            try
            {
                using (StreamReader sr = new StreamReader(fileName))
                {
                    while ((line = sr.ReadLine()) != null)
                    {
                        insertQuery(table, line);
                    }
                    Debug.WriteLine("Plik " + fileName + " wczytano pomyślnie.");
                }
            }
            catch (Exception e)
            {
                Debug.WriteLine("Plik nie mógł zostać wczytany: ");
                Debug.WriteLine(e.Message);
            }
         }
    }
}
