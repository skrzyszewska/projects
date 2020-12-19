using Gra_Państwa_Miasta.ClientServer;
using Gra_Państwa_Miasta.Dialog;
using System.Windows;
using Gra_Państwa_Miasta.Documentations;

namespace Gra_Państwa_Miasta.Pages
{
    public partial class HomePage
    {
        #region Fields

        private MakeGame m_MakerOfNewGame;
        private IPlayer Server;

        #endregion

        #region Action

        public delegate void HomePageEventHandler(object source, IPlayer server, IPlayer client = null);
        public event HomePageEventHandler CreateServer, AddClient;

        // Action Method
        private void OnCreateGame(object source, string name, string adress, string port)
        {
            m_MakerOfNewGame.Close();
            m_MakerOfNewGame = null;

            Server = new ServerObject(1, Login.Text, "Klient/Server", name, adress, port);
            if (Server.TryAddClient(Server))
            {
                AvailablePlayers.Items.Clear();
                AvailablePlayers.Items.Add(Server);
            }

            AvailablePlayers.Visibility = Visibility.Visible;
            EmptyList.Visibility = Visibility.Collapsed;
            ChangeVisibilityOfButtons();

            CreateServer?.Invoke(this, Server);

            m_MakerOfNewGame = new MakeGame();
        }

        #endregion

        #region Constructors

        public HomePage()
        {
            InitializeComponent();

            Init();

            Description.Text = eRessource.Description;
            Rules.Text = eRessource.Rules;
        }

        private void Init()
        {
            if (Server != null && Server.ListOfClient.Count > 0)
            {
                for (var i = 0; i < Server.ListOfClient.Count; i++)
                {
                    AvailablePlayers.Items.Add(Server.ListOfClient);
                }

                ChangeVisibilityOfButtons();
            }
            else
            {
                if (AvailablePlayers.Items.IsEmpty)
                {
                    AvailablePlayers.Visibility = Visibility.Collapsed;
                    EmptyList.Visibility = Visibility.Visible;
                }
            }

            m_MakerOfNewGame = new MakeGame();
        }

        #endregion

        #region Button Click Method

        private void JoinOrCreate_Click(object sender, RoutedEventArgs e)
        {
            if (AvailablePlayers.Items.IsEmpty)
            {
                if (Login.Text.Length > 0 && !Login.Text.Equals("Podaj swój login"))
                {
                    if(m_MakerOfNewGame == null) m_MakerOfNewGame = new MakeGame();

                    m_MakerOfNewGame.CreateNewGame -= OnCreateGame;
                    m_MakerOfNewGame.CreateNewGame += OnCreateGame;
                    m_MakerOfNewGame.Show();
                }
                else
                {
                    MessageBox.Show("Podaj Login!", "Error");
                }
            }
            else
            {
                var count = Server.ListOfClient.Count + 1;
                var newClient = new ClientObject(count, Login.Text, "Klient", Server.NameOfGame, Server.Adress, Server.Port);
                if(Server.TryAddClient(newClient))
                {
                    AvailablePlayers.Items.Add(newClient);
                    AddClient?.Invoke(this, Server, newClient);
                }
                else
                {
                    MessageBox.Show("Zbyt wielu graczy!", "Error");
                }
            }
        }
        
        private void Join_Click(object sender, RoutedEventArgs e)
        {
            var count = Server.ListOfClient.Count + 1;
            var newClient = new ClientObject(count, Login.Text, "Klient", Server.NameOfGame, Server.Adress, Server.Port);
            if (Server.TryAddClient(newClient))
            {
                AvailablePlayers.Items.Add(newClient);
                AddClient?.Invoke(this, Server, newClient);
            }
            else
            {
                MessageBox.Show("Zbyt wielu graczy!", "Error");
            }
        }

        private void Create_Click(object sender, RoutedEventArgs e)
        {
            if (Login.Text.Length > 0 && !Login.Text.Equals("Podaj swój login"))
            {
                if (m_MakerOfNewGame == null) m_MakerOfNewGame = new MakeGame();

                m_MakerOfNewGame.CreateNewGame -= OnCreateGame;
                m_MakerOfNewGame.CreateNewGame += OnCreateGame;
                m_MakerOfNewGame.Show();
            }
            else
            {
                MessageBox.Show("Podaj Login!", "Error");
            }

        }

        #endregion

        #region Focus Control Method

        private void Login_MouseDown(object sender, RoutedEventArgs e)
        {
            Login.Text = "";
        }

        private void Login_LostFocus(object sender, RoutedEventArgs e)
        {
            if (Login.Text.Length == 0)
            {
                Login.Text = "Podaj swój login";
            }
        }

        #endregion

        #region Private Method

        private void ChangeVisibilityOfButtons()
        {
            FirstPlayerOn.Visibility = FirstPlayerOn.Visibility == Visibility.Visible ? Visibility.Collapsed : Visibility.Visible;
            AnyServerIsAvailable.Visibility = AnyServerIsAvailable.Visibility == Visibility.Visible ? Visibility.Collapsed : Visibility.Visible;
        }

        #endregion
    }
}
