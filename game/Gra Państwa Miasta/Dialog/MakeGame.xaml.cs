using System;
using System.Windows;
using System.Windows.Input;
using Gra_Państwa_Miasta.Helper;

namespace Gra_Państwa_Miasta.Dialog
{
    public partial class MakeGame : Window
    {
        #region Public Method

        public delegate void MakeGameEventHandler(object source, string name, string adress, string port);
        public event MakeGameEventHandler CreateNewGame;

        public void SetNameOfNewGame(string text, string adress, string port)
        {
            CreateNewGame?.Invoke(this, text, adress, port);
        }

        #endregion

        #region Construktor
        
        public MakeGame()
        {
            InitializeComponent();
            AdressOfNewGame.Text = eHelper.GetLocalIPAddress();
            if (AdressOfNewGame.Text.Equals("Brak adresu ipv4"))
            {
                MessageBox.Show(AdressOfNewGame.Text);
                Close();
            }
        }

        #endregion

        #region Private Method

        #region Click Method

        private void Create_Click(object sender, RoutedEventArgs e)
        {
            if (NameOfNewGame.Text.Length > 0 && !NameOfNewGame.Text.Equals("Podaj swój login"))
            {
                if (PortOfNewGame.Text.Length >= 4 && PortOfNewGame.Text.Length <= 5 &&
                    !PortOfNewGame.Text.Equals("port [1025-65535]") && Int32.TryParse(PortOfNewGame.Text, out _))
                {
                    SetNameOfNewGame(NameOfNewGame.Text, AdressOfNewGame.Text, PortOfNewGame.Text);
                }
                else
                {
                    MessageBox.Show("Podaj port");
                }
            }
            else
            {
                MessageBox.Show("Podaj Nazwe Gry");
            }
        }

        private void KolumnyCheck_OnClick(object sender, RoutedEventArgs e)
        {
            if (KolumnyCheck.IsChecked == true)
            {
                Kolumny.Visibility = Visibility.Visible;
                Height = ActualHeight + 150;
                MainGrid.Height = MainGrid.ActualHeight + 150;
            }
            else
            {
                Kolumny.Visibility = Visibility.Collapsed;
                Height = ActualHeight - 150;
                MainGrid.Height = MainGrid.ActualHeight - 150;
            }
        }

        private void CzasCheck_OnClick(object sender, RoutedEventArgs e)
        {
            if (Czas.IsChecked == true)
            {
                CzasGrid.Visibility = Visibility.Visible;
                Height = ActualHeight + 40;
                MainGrid.Height = MainGrid.ActualHeight + 40;
            }
            else
            {
                CzasGrid.Visibility = Visibility.Collapsed;
                Height = ActualHeight - 40;
                MainGrid.Height = MainGrid.ActualHeight - 40;
            }
        }

        #endregion

        #region Focus Method

        private void NameOfNewGame_MouseDown(object sender, RoutedEventArgs e)
        {
            NameOfNewGame.Text = "";
        }

        private void NameOfNewGame_LostFocus(object sender, RoutedEventArgs e)
        {
            if (NameOfNewGame.Text.Length == 0)
            {
                NameOfNewGame.Text = "Podaj swój login";
            }
        }

        private void PortOfNewGame_OnPreviewMouseDown(object sender, MouseButtonEventArgs e)
        {
            PortOfNewGame.Text = "";
        }

        private void PortOfNewGame_OnLostFocus(object sender, RoutedEventArgs e)
        {
            if (PortOfNewGame.Text.Length == 0)
            {
                PortOfNewGame.Text = "port [1025-65535]";
            }
            else
            {
                if (!Int32.TryParse(PortOfNewGame.Text, out _))
                {
                    PortOfNewGame.Text = "port [1025-65535]";
                    MessageBox.Show("Port musi być liczbą z zakresu [1025-65535]");
                }
            }

        }

        #endregion

        #endregion
    }
}
