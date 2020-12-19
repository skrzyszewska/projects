namespace Gra_Państwa_Miasta.Documentations
{
    public static class eRessource
    {
        #region Public Const Ressources

        public const string Description = 
            "Gra jest przeznaczona dla graczy w liczbie od 2 do 6 ." +
            "\nPrzed rozpoczeciem gry podaj nick" +
            "\n    Jezeli jestes graczem po prostu dolaczasz do istniejacej gry, w ktorej jest miejsce" +
            "\n    Jezeli jestes organizatorem gry :" +
            "\nStworzenie gry:" +
            "\n" +
            "\nPodajesz nazwe gry" +
            "\n    Decydujesz kiedy rozpoczyna sie gra" +
            "\n    Decydujesz ile osob moze dolaczyc do gry" +
            "\n" +
            "\nKolumny tabelki są zatytułowane nazwami poszczególnych kategorii do wybrania,minimum dwie do zaznaczenia,domyslnie sa wszystkie zaznaczone:" +
            "\n    Państwo" +
            "\n    Miasto" +
            "\n    Zwierzę" +
            "\n    Rzecz" +
            "\n    Imię" +
            "\n" +
            "\nWybierasz tryb gry:" +
            "\n    Na czas" +
            "\n    Na ilosc rund(do wpisania)" +
            "\n    Na iosc  punktow" +
            "\n * Czy chcesz dogrywke podczas remisu?" +
            "\n" +
            "\nModyfikacja punktow(opcjonalnie):" +
            "\n    - pkt - Jeżeli gracz jako jedyny miał wyraz." +
            "\n    - pkt - Jeżeli wyraz się nie powtorzył." +
            "\n    - pkt - Jeżeli wyraz sie powtorzył." +
            "\n    - pkt - Jeżeli wyraz był niepoprawny lub jego brak.";

        public const string Rules =
            "1. Gra rozpoczyna się od wylosowania przez jednego z graczy litery alfabetu. Z losowania wyłączone są takie literki jak : Ą, Ę, Ń, Ó, Ź ,Ż Q, V, X Y." +
            "\n" +
            "\n 2. Od momentu wylosowania litery wszyscy gracze zaczynają wpisywać do swoich tabelek słowa zaczynające się na wylosowaną literę – do każdej kolumny tabelki po jednym słowie pasującym do danej kategorii.Wpisywanie słów kończy się, gdy któryś z graczy wpisze odpowiednie słowa do wszystkich kolumn tabelki i naciśnie przycisk STOP, który rozpocznie odliczanie ostatnich chwil na dopisywanie odpowiedzi." +
            "\n" +
            "\n3. Zliczanie punktów." +
            "\n   - 15 pkt - Jeżeli gracz jako jedyny miał wyraz." +
            "\n   - 10 pkt - Jeżeli wyraz się nie powtorzył." +
            "\n   -  5 pkt - Jeżeli wyraz sie powtorzył." +
            "\n   -  0 pkt - Jeżeli wyraz był niepoprawny lub jego brak." +
            "\n4. Gra trwa tyle rund ile wcześniej zostało ustalone. Wygrywa ten, kto zbierze w trakcie gry najwięcej punktów. " +
            "\n";

        #endregion
    }
}
