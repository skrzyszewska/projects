using System.Collections.Generic;
using System.Diagnostics;

namespace Gra_Państwa_Miasta.ClientServer
{
    public class ServerObject : IPlayer
    {
        private const int MAX_CLIENT_COUNT = 6;
        public int Number { get; set; }
        public string NameOfGame { get; set; }
        public string Login { get; set; }
        public string Type { get; set; }
        public string Adress { get; set; }
        public string Port { get; set; }

        public List<IPlayer> ListOfClient { get; set; }

        public ServerObject(int nr, string login, string type, string name, string adress, string port)
        {
            Number = nr;
            Login = login;
            Type = type;
            NameOfGame = name;
            Adress = adress;
            Port = port;
            ListOfClient = new List<IPlayer>(6);
        }

        public bool TryAddClient(IPlayer client)
        {
            if(client.Number < MAX_CLIENT_COUNT + 1)
            {
                ListOfClient.Add(client);
                return true;
            }
            Debug.WriteLine("Zbyt wielku graczy");
            return false;
        }

        public bool TryRemoveClient(IPlayer client)
        {
            if(ListOfClient.Remove(client))
            {
                return true;
            }
            Debug.WriteLine($"Nie znaloziono lub problem z usunięciam gracza o loginie: {client.Login}");
            return false;
        }
    }
}
