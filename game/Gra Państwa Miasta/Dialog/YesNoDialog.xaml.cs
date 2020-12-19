using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Gra_Państwa_Miasta.Dialog
{
    /// <summary>
    /// Interaction logic for YesNoDialog.xaml
    /// </summary>
    public partial class YesNoDialog : Window
    {
        public delegate void MakeGameEventHandler(object source, bool yes_no);
        public event MakeGameEventHandler Answer;

        public YesNoDialog(string question)
        {
            InitializeComponent();

            Question.Text = $"\n\n{question}\n\n";
        }

        private void No_Click(object sender, RoutedEventArgs e)
        {
            SetNameOfNewGame(false);
        }

        private void Yes_Click(object sender, RoutedEventArgs e)
        {
            SetNameOfNewGame(true);
        }

        public void SetNameOfNewGame(bool yes_no)
        {
            Answer?.Invoke(this, yes_no);
        }
    }
}
