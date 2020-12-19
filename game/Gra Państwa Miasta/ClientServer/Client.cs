using System.Collections.Generic;

namespace Gra_Państwa_Miasta.ClientServer
{
    public class ClientObject : IPlayer
    {
        public int Number { get; set; }
        public string NameOfGame { get; set; }
        public string Login { get; set; }
        public string Type { get; set; }
        public string Adress { get; set; }
        public string Port { get; set; }

        public List<IPlayer> ListOfClient
        {
            get => throw new System.NotImplementedException();
            set => throw new System.NotImplementedException();
        }

        public ClientObject(int nr, string login, string type, string name, string adress, string port)
        {
            Number = nr;
            Login = login;
            Type = type;
            NameOfGame = name;
            Adress = adress;
            Port = port;
        }

        public bool TryAddClient(IPlayer client)
        {
            throw new System.NotImplementedException();
        }

        public bool TryRemoveClient(IPlayer client)
        {
            throw new System.NotImplementedException();
        }
    }
}
