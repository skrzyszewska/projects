using System.Windows.Controls;
using Gra_Państwa_Miasta.Documentations;

namespace Gra_Państwa_Miasta.Pages
{
    public partial class HelpPage : Page
    {
        public HelpPage()
        {
            InitializeComponent();
            
            Description.Text = eRessource.Description;
            Rules.Text = eRessource.Rules;
        }
    }
}
