import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class main {

    /* Czysci terminal odrazu */
    public static void m_fast_clean(){

        for (int i = 0; i < 50; i++) System.out.println("");
    }

    private static void m_rozeslij_pola(Socket[] tab_s, Pola[] tab_p, int liczba_graczy) throws IOException {

        ObjectOutputStream tab[] = new ObjectOutputStream[liczba_graczy];
        for (int i = 0; i < liczba_graczy; i++) {

            tab[i] = new ObjectOutputStream (tab_s[i].getOutputStream());
            Thread t = new Client_SendBoardData(tab_s[i], tab[i], tab_p);
            t.start();
        }
    }

    private static int m_rzutKostka(){
        int liczba = 0;
        liczba = (int) Math.random();
        liczba = liczba % 6;

        if (liczba == 0) liczba = 6;
        return liczba;
    }

    private static void m_rozloz_pionki_start( Gracz[] tab_g, Pola[] tab_p){
        tab_p[0].setStojacy_na_nim_pionek(tab_g[0].getTablica_pionkow()[0]);
        tab_p[1].setStojacy_na_nim_pionek(tab_g[0].getTablica_pionkow()[1]);
        tab_p[2].setStojacy_na_nim_pionek(tab_g[0].getTablica_pionkow()[2]);
        tab_p[3].setStojacy_na_nim_pionek(tab_g[0].getTablica_pionkow()[3]);
        tab_p[4].setStojacy_na_nim_pionek(tab_g[1].getTablica_pionkow()[0]);
        tab_p[5].setStojacy_na_nim_pionek(tab_g[1].getTablica_pionkow()[1]);
        tab_p[6].setStojacy_na_nim_pionek(tab_g[1].getTablica_pionkow()[2]);
        tab_p[7].setStojacy_na_nim_pionek(tab_g[1].getTablica_pionkow()[3]);
        tab_p[8].setStojacy_na_nim_pionek(tab_g[2].getTablica_pionkow()[0]);
        tab_p[9].setStojacy_na_nim_pionek(tab_g[2].getTablica_pionkow()[1]);
        tab_p[10].setStojacy_na_nim_pionek(tab_g[2].getTablica_pionkow()[2]);
        tab_p[11].setStojacy_na_nim_pionek(tab_g[2].getTablica_pionkow()[3]);
        tab_p[12].setStojacy_na_nim_pionek(tab_g[3].getTablica_pionkow()[0]);
        tab_p[13].setStojacy_na_nim_pionek(tab_g[3].getTablica_pionkow()[1]);
        tab_p[14].setStojacy_na_nim_pionek(tab_g[3].getTablica_pionkow()[2]);
        tab_p[15].setStojacy_na_nim_pionek(tab_g[3].getTablica_pionkow()[3]);
    }

    private static boolean m_sprawdz_warunek_wygranej(Gracz[] tab_p, int liczbagraczy){
        for (int i = 0; i < liczbagraczy; i++){
            if (tab_p[i].getLiczba_pionkow_na_finishu() == 4) return false;
        }
        return true;
    }

    /* Czeka Z sekund wyswietlajac Z kropek */
    public static void m_count(int z){

        z+=1;

        try {
            for (int i = 0; i < z; i++){
                TimeUnit.SECONDS.sleep(1);
                System.out.print(".");
            }
            System.out.println("");
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws IOException {

        m_fast_clean();

        ServerSocket ss = new ServerSocket(5056);
        int liczba_graczy = 0;

        /* Sokety zawierajace do 4 graczy */
        Socket[] tab_s = new Socket[4];

        /* Tablica graczy zawierajaca wszystkich 4 graczy */
        Gracz[] tab_g = new Gracz[4];
        Pola[] tab_p = new Pola[72];

        String kolor;

        /* Odebrane zostanie polaczenie tylko od 4 uzytkownikow max */
        while (liczba_graczy != 4) {
            Socket s = null;
            try {

                /* Odbieramy polaczenie od klienta */
                s = ss.accept();
                tab_s[liczba_graczy] = s;

                System.out.println("Polaczono z nowym uzytkownikiem: " + s);

                DataInputStream dis = new DataInputStream(tab_s[liczba_graczy].getInputStream());
                DataOutputStream dos = new DataOutputStream(tab_s[liczba_graczy].getOutputStream());

                System.out.println("Rozpoczynam przypisywanie mu numeru");
                Thread t = new Client_GiveNumber(tab_s[liczba_graczy], dis, dos, liczba_graczy);
                t.start();

                System.out.println("Odczytuje kolor gracza");
                kolor = dis.readUTF();

                tab_g[liczba_graczy] = new Gracz(kolor, liczba_graczy);
                liczba_graczy++;
            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }

        System.out.println("W grze znajduja sie nastepujacy gracze: ");
        for (int i = 0; i < liczba_graczy; i++){
            System.out.println(tab_g[i]);
        }

        ObjectOutputStream tab_1[] = new ObjectOutputStream[liczba_graczy];
        /* Rozeslij wszystkim graczom informacje o wszystkich innych graczach */
        for (int i = 0; i < liczba_graczy; i++) {

            //ObjectInputStream dis = new ObjectInputStream(tab_s[i].getInputStream());
            tab_1[i] = new ObjectOutputStream (tab_s[i].getOutputStream());

            /* Uruchom watek dla poszczegolnych graczy */
            Thread t = new Client_SendPlayersData(tab_s[i], tab_1[i], tab_g);
            t.start();
        }

        m_count(3);

        String barwa_pola;

        System.out.println("Tworze pola dla wszystkich pionkow oraz ustawiam pionki graczy na pola poczatkowe");
        /*  Pola 0-3    / 56-59 - kolor gracza 1
            Pola 4-7    / 60-63 - kolor gracza 2
            Pola 8-11   / 64-67 - kolor gracza 3
            Pola 12-15  / 68-71 - kolor gracza 4 */

        for (int i = 0; i < 72; i++){

            barwa_pola = "bialy";
            if ( (i <= 3) || (i>= 56 && i <= 59) ){
                barwa_pola = tab_g[0].getKolor();
            }
            if ( (i>= 4 && i <= 7) || (i>= 60 && i <= 63) ){
                barwa_pola = tab_g[1].getKolor();
            }
            if ( (i>= 8 && i <= 11) || (i>= 64 && i <= 67) ){
                barwa_pola = tab_g[2].getKolor();
            }
            if ( (i>= 12 && i <= 15) || (i>= 68) ){
                barwa_pola = tab_g[3].getKolor();
            }
            tab_p[i] = new Pola(barwa_pola, i, null);
        }

        /* Sprawdzanie pól */
        for (int i = 0; i < 72; i++){
            System.out.println(tab_p[i]);
        }

        m_rozeslij_pola(tab_s, tab_p, liczba_graczy);

        m_count(3);

        /* Rozkladamy pionki na pola startowe */
        m_rozloz_pionki_start(tab_g, tab_p);

        /* Rozsylamy aktualizacje o rozstawieniu pionkow */
        m_rozeslij_pola(tab_s, tab_p, liczba_graczy);

        for (int i = 0; i < 72; i++){
            System.out.println(tab_p[i]);
        }

        int liczba_wyrzuconych_oczek = 0;
        int numer_aktualnie_grajacego_gracza = 0;
        int liczba_rzutow = 0;

        /* Rozeslij informacje o tym, kto zaczyna gre */


        /* Rozstawione zostaly pionki - przechodzimy do wykonywania rzutu kostka
           - Poki nie zostanie wyrzucona 6 - pionek nie moze wyjsc z bazy */
        while (m_sprawdz_warunek_wygranej(tab_g, liczba_graczy)){

            /* Jesli ma 4 pionkow w bazie - musi wyrzucic 6, ma na to 3 szanse. Po wyrzuceniu szostki rzuca jeszcze raz */
            if (tab_g[numer_aktualnie_grajacego_gracza].getLiczba_pionkow_w_bazie() == 4 && liczba_rzutow < 3){

                liczba_wyrzuconych_oczek = m_rzutKostka();
                liczba_rzutow++;
                /* Rozeslij liczbe wyrzuconych oczek */

                if (liczba_wyrzuconych_oczek == 6){
                    /* Gracz wyrzucil 6! Ustawiamy jego pionek z pola bazy na pole startowe */

                }

            /* Gracz ma jakiegos pionka w bazie i na planszy - on wybiera, ktorym chce dokonac nastepny ruch */
            } if (tab_g[numer_aktualnie_grajacego_gracza].getLiczba_pionkow_w_bazie() < 4 && tab_g[numer_aktualnie_grajacego_gracza].getLiczba_pionkow_w_bazie() > 0 &&  liczba_rzutow < 3){

                liczba_wyrzuconych_oczek = m_rzutKostka();
                liczba_rzutow++;
                /* Rozeslij liczbe wyrzuconych oczek */


                if (liczba_wyrzuconych_oczek == 6 && /* Na polu startowym nie stoi pionek, ktory ma ten sam kolor gracza*/ true) {
                    /* Odczytaj od gracza, czy chce wypuscic pionka z bazy czy chce sie ruszyc pionkiem na planszy */

                    if (/* Jesli wybral pionka z bazy - rusz sie pionkiem z bazy na pole startowe */true){

                    } else {

                    }
                }

            /* Gracz ma tylko pionki na planszy i nie ukonczyl gry */
            } else if (tab_g[numer_aktualnie_grajacego_gracza].getLiczba_pionkow_w_bazie() == 0 &&  liczba_rzutow < 3) {

                liczba_wyrzuconych_oczek = m_rzutKostka();
                liczba_rzutow++;
                /* Rozeslij liczbe wyrzuconych oczek */


                /* Odczytywanie, ktorym pionkiem rusza sie gracz */

            }
            /* Tura przechodzi do kolejnego gracza, jesli nie wyrzucona zostala 6tka -
            zerujemy liczbe rzutow oraz ustawiamy numer gracza aktualnie wybierajacego pionki na planszy */
            if (liczba_wyrzuconych_oczek != 6 ||  liczba_rzutow == 3) {
                liczba_rzutow = 0;
                numer_aktualnie_grajacego_gracza++;
                if (numer_aktualnie_grajacego_gracza == liczba_graczy)
                    numer_aktualnie_grajacego_gracza = 0;

                /* Poinformuj graczy, kto teraz rozpoczyna rzut */


            }

            /* Sposob przeprowadzania rozgrywki:
            - 1. Wyrzuc liczbe oczek
            - 2. Jesli gracz nie ma pionkow na planszy (liczba pionkow w bazie = 4) to:
               - jesli wyrzuci 6 - pierwszy pionek idzie na jego pole startowe
               - jesli nie wyrzuci 6 - ma dwie pozostale szanse
            - 3. Jesli wyrzucil 6tke, i jego pionek idzie na pole startowe, trzeba sprawdzic, czy na tym polu nie stoi
                 inny pionek. Jesli stoi - jest kasowany (wraca do bazy).
            - 4. Jesli wyrzucil 6tke i ma pionek na planszy - moze wybrac albo pionka na planszy, albo pionka w bazie i nim sie
                 ruszyc. Trzeba sprawdzic, czy pionek nie zabil innego pionka
            - 5. Jesli wyrzucil jakas inna liczbe oczek - moze wybrac tylko pionka na planszy
            - 6. W momencie gdy znajduje sie jego pionek na polach najblizszych jego finishu - musi wyrzucic odpowiednia liczbe oczek
                 ktora nie spowoduje "wyjsciu" poza swoje granice finishu */

        }

    }
}