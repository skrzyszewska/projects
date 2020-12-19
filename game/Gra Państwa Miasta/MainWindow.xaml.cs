using Gra_Państwa_Miasta.ClientServer;
using Gra_Państwa_Miasta.Dialog;
using Gra_Państwa_Miasta.Pages;
using System.Windows;
using System.Windows.Controls;

namespace Gra_Państwa_Miasta
{
    public partial class MainWindow : Window
    {
        #region Fields

        private Page lastPage;
        private HomePage Home;
        private IPlayer Server;
        private YesNoDialog Question;

        #endregion

        #region Constructors

        public MainWindow()
        {
            InitializeComponent();

            Init();
        }

        private void Init()
        {
            HelpButton.Visibility = Visibility.Visible;
            HelpBackButton.Visibility = Visibility.Collapsed;

            lastPage = null;
            Home = new HomePage();
            MainFrame.Content = Home;
            Home.CreateServer -= Home_CreateServer;
            Home.CreateServer += Home_CreateServer;

            Home.AddClient -= Home_AddClient;
            Home.AddClient += Home_AddClient;
        }

        #endregion

        #region Private Method
        
        private void Home_AddClient(object source, IPlayer server, IPlayer client)
        {
            Server = server;
            SetNick(client.Login);
            MainFrame.Content = new WaitingRoom();
        }

        private void SetNick(string nick)
        {
            Nick.Text = nick;
        }

        private void Home_CreateServer(object source, IPlayer server, IPlayer client = null)
        {
            Server = server;
            SetNick(Server.Login);
            //MainFrame.Content = new WaitingRoom();
        }

        private void Question_Answer(object source, bool yes_no)
        {
            Question.Close();
            Question.Answer -= Question_Answer;

            if (yes_no)
            {
                HelpButton.Visibility = Visibility.Visible;
                HelpBackButton.Visibility = Visibility.Collapsed;

                lastPage = null;
                Home.CreateServer -= Home_CreateServer;

                Home = new HomePage();
                MainFrame.Content = Home;
                Home.CreateServer += Home_CreateServer;
            }
        }

        #endregion

        #region Button Click Method

        private void CloseMenuButton_Click(object sender, RoutedEventArgs e)
        {
            OpenMenuButton.Visibility = Visibility.Visible;
            CloseMenuButton.Visibility = Visibility.Collapsed;
            Icon.Visibility = Visibility.Collapsed;
        }

        private void OpenMenuButton_Click(object sender, RoutedEventArgs e)
        {
            OpenMenuButton.Visibility = Visibility.Collapsed;
            CloseMenuButton.Visibility = Visibility.Visible;
            Icon.Visibility = Visibility.Visible;
        }

        private void HomeButton_Click(object sender, RoutedEventArgs e)
        {
            Question = new YesNoDialog("Czy na pewno chcesz przejść do strony domowej?\nBedzie to oznaczać wyjście z bierzacej rozgrywki");
            Question.Answer += Question_Answer;
            Question.Show();
        }
        
        private void HelpButton_Click(object sender, RoutedEventArgs e)
        {
            if(HelpButton.Visibility == Visibility.Visible)
            {
                HelpButton.Visibility = Visibility.Collapsed;
                HelpBackButton.Visibility = Visibility.Visible;

                if (lastPage != null)
                {
                    MainFrame.Content = lastPage;
                }
                else
                {
                    lastPage = MainFrame.Content as Page;
                    MainFrame.Content = new HelpPage();
                }
            }
            else
            {
                HelpButton.Visibility = Visibility.Visible;
                HelpBackButton.Visibility = Visibility.Collapsed;

                if(lastPage != null)
                {
                    MainFrame.Content = lastPage;
                }
                else
                {
                    MessageBox.Show("Coś poszło nie tak :/", "Error");
                    MainFrame.Content = new HomePage();
                }
            }
        }

        #endregion
    }
}
