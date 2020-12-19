using System.Collections.Generic;

namespace Gra_Państwa_Miasta.ClientServer
{
    public interface IPlayer
    {
        int Number { get; set; }
        string NameOfGame { get; set; }
        string Login { get; set; }
        string Type { get; set; }
        string Adress { get; set; }
        string Port { get; set; }
        List<IPlayer> ListOfClient { get; set; }

        bool TryAddClient(IPlayer client);

        bool TryRemoveClient(IPlayer client);
    }
}
