import java_pliki.Gracz;
import java_pliki.Pola;

import java.io.*;
import java.net.*;

public class main {

    /* Czysci terminal odrazu */
    public static void m_fast_clean(){

        for (int i = 0; i < 50; i++) System.out.println("");
    }

    private static int m_rzutKostka(){
        return (int)(Math.random() * ((6 - 1) + 1)) + 1;
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

        ObjectInputStream []dis = new ObjectInputStream[4];
        ObjectOutputStream []dos = new ObjectOutputStream[4];

        /* Odebrane zostanie polaczenie tylko od 4 uzytkownikow max */
        while (liczba_graczy != 4) {
            Socket s = null;
            try {

                /* Odbieramy polaczenie od klienta */
                s = ss.accept();
                tab_s[liczba_graczy] = s;

                System.out.println("Polaczono z nowym uzytkownikiem: " + s);

                ObjectOutputStream dos_temp = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream dis_temp = new ObjectInputStream(s.getInputStream());

                dis[liczba_graczy] = dis_temp;
                dos[liczba_graczy] = dos_temp;

                System.out.println("Rozpoczynam przypisywanie mu numeru");
                dos_temp.writeObject(liczba_graczy);
                dos_temp.flush();

                System.out.println("Odczytuje kolor gracza");
                kolor = (String) dis[liczba_graczy].readObject();

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

        /* Rozeslij wszystkim graczom informacje o wszystkich innych graczach */
        for (int i = 0; i < liczba_graczy; i++){
            Thread t = new Client_SendPlayersData(tab_s[i], dos[i], tab_g);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String barwa_pola;

        System.out.println("Tworze pola dla wszystkich pionkow oraz ustawiam pionki graczy na pola poczatkowe");
        /*  java_pliki.Pola 0-3    / 56-59 - kolor gracza 1
            java_pliki.Pola 4-7    / 60-63 - kolor gracza 2
            java_pliki.Pola 8-11   / 64-67 - kolor gracza 3
            java_pliki.Pola 12-15  / 68-71 - kolor gracza 4 */

        for (int i = 0; i < 72; i++){

            barwa_pola = "bialy";
            if ( (i <= 3) || (i>= 56 && i <= 59) ){
                barwa_pola = tab_g[0].getNazwa_gracza();
            }
            if ( (i>= 4 && i <= 7) || (i>= 60 && i <= 63) ){
                barwa_pola = tab_g[1].getNazwa_gracza();
            }
            if ( (i>= 8 && i <= 11) || (i>= 64 && i <= 67) ){
                barwa_pola = tab_g[2].getNazwa_gracza();
            }
            if ( (i>= 12 && i <= 15) || (i>= 68) ){
                barwa_pola = tab_g[3].getNazwa_gracza();
            }
            tab_p[i] = new Pola(barwa_pola, i, null);
        }

        set_x_y(tab_p);
        /* Sprawdzanie pól */
        for (int i = 0; i < 72; i++){
            System.out.println(tab_p[i]);
        }

        /* Rozkladamy pionki na pola startowe */
        m_rozloz_pionki_start(tab_g, tab_p);

        /* Rozeslij wszystkim graczom informacje o wszystkich innych graczach */
        for (int i = 0; i < liczba_graczy; i++){
            Thread t = new Client_SendBoardData(tab_s[i], dos[i], tab_p);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 72; i++){
            System.out.println(tab_p[i]);
        }

        int liczba_wyrzuconych_oczek = 0;
        int numer_aktualnie_grajacego_gracza = 0;
        int liczba_rzutow = 0;

        liczba_wyrzuconych_oczek = m_rzutKostka();
        System.out.println("Wyrzuciłem: " + liczba_wyrzuconych_oczek);

        /* Rozsyłam liczbę rzuconych oczek */
        for (int i = 0; i < liczba_graczy; i++){
            Thread t = new Client_SendPlayerKostka(tab_s[i], dos[i], liczba_wyrzuconych_oczek);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* Rozstawione zostaly pionki - przechodzimy do wykonywania rzutu kostka
           - Poki nie zostanie wyrzucona 6 - pionek nie moze wyjsc z bazy */
        while (m_sprawdz_warunek_wygranej(tab_g, liczba_graczy)){

            /* Jesli ma 4 pionkow w bazie - musi wyrzucic 6, ma na to 3 szanse. Po wyrzuceniu szostki rzuca jeszcze raz */
            if (tab_g[numer_aktualnie_grajacego_gracza].getLiczba_pionkow_w_bazie() == 4 && liczba_rzutow < 3){


                liczba_rzutow++;
                /* Rozeslij liczbe wyrzuconych oczek */

                if (liczba_wyrzuconych_oczek == 6){
                    /* java_pliki.Gracz wyrzucil 6! Ustawiamy jego pionek z pola bazy na pole startowe */

                }

            /* java_pliki.Gracz ma jakiegos pionka w bazie i na planszy - on wybiera, ktorym chce dokonac nastepny ruch */
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

            /* java_pliki.Gracz ma tylko pionki na planszy i nie ukonczyl gry */
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

    private static void set_x_y(Pola[] tab_p) {
        tab_p[0].setPosition_x(100);
        tab_p[0].setPosition_y(372);
        tab_p[1].setPosition_x(158);
        tab_p[1].setPosition_y(373);
        tab_p[2].setPosition_x(100);
        tab_p[2].setPosition_y(409);
        tab_p[3].setPosition_x(158);
        tab_p[3].setPosition_y(409);
        tab_p[4].setPosition_x(509);
        tab_p[4].setPosition_y(377);
        tab_p[5].setPosition_x(567);
        tab_p[5].setPosition_y(377);
        tab_p[6].setPosition_x(506);
        tab_p[6].setPosition_y(417);
        tab_p[7].setPosition_x(564);
        tab_p[7].setPosition_y(417);
        tab_p[8].setPosition_x(94);
        tab_p[8].setPosition_y(645);
        tab_p[9].setPosition_x(154);
        tab_p[9].setPosition_y(645);
        tab_p[10].setPosition_x(94);
        tab_p[10].setPosition_y(688);
        tab_p[11].setPosition_x(154);
        tab_p[11].setPosition_y(688);
        tab_p[12].setPosition_x(501);
        tab_p[12].setPosition_y(656);
        tab_p[13].setPosition_x(560);
        tab_p[13].setPosition_y(656);
        tab_p[14].setPosition_x(501);
        tab_p[14].setPosition_y(695);
        tab_p[15].setPosition_x(561);
        tab_p[15].setPosition_y(695);
        tab_p[16].setPosition_x(41);
        tab_p[16].setPosition_y(484);
        tab_p[17].setPosition_x(100);
        tab_p[17].setPosition_y(484);
        tab_p[18].setPosition_x(159);
        tab_p[18].setPosition_y(484);
        tab_p[19].setPosition_x(218);
        tab_p[19].setPosition_y(484);
        tab_p[20].setPosition_x(277);
        tab_p[20].setPosition_y(484);
        tab_p[21].setPosition_x(277);
        tab_p[21].setPosition_y(447);
        tab_p[22].setPosition_x(277);
        tab_p[22].setPosition_y(410);
        tab_p[23].setPosition_x(277);
        tab_p[23].setPosition_y(373);
        tab_p[24].setPosition_x(277);
        tab_p[24].setPosition_y(336);
        tab_p[25].setPosition_x(336);
        tab_p[25].setPosition_y(336);
        tab_p[26].setPosition_x(395);
        tab_p[26].setPosition_y(336);
        tab_p[27].setPosition_x(395);
        tab_p[27].setPosition_y(373);
        tab_p[28].setPosition_x(395);
        tab_p[28].setPosition_y(410);
        tab_p[29].setPosition_x(395);
        tab_p[29].setPosition_y(447);
        tab_p[30].setPosition_x(395);
        tab_p[30].setPosition_y(484);
        tab_p[31].setPosition_x(454);
        tab_p[31].setPosition_y(484);
        tab_p[32].setPosition_x(513);
        tab_p[32].setPosition_y(484);
        tab_p[33].setPosition_x(572);
        tab_p[33].setPosition_y(484);
        tab_p[34].setPosition_x(631);
        tab_p[34].setPosition_y(484);
        tab_p[35].setPosition_x(631);
        tab_p[35].setPosition_y(521);
        tab_p[36].setPosition_x(631);
        tab_p[36].setPosition_y(558);
        tab_p[37].setPosition_x(572);
        tab_p[37].setPosition_y(558);
        tab_p[38].setPosition_x(513);
        tab_p[38].setPosition_y(558);
        tab_p[39].setPosition_x(454);
        tab_p[39].setPosition_y(558);
        tab_p[40].setPosition_x(395);
        tab_p[40].setPosition_y(558);
        tab_p[41].setPosition_x(395);
        tab_p[41].setPosition_y(595);
        tab_p[42].setPosition_x(395);
        tab_p[42].setPosition_y(632);
        tab_p[43].setPosition_x(395);
        tab_p[43].setPosition_y(669);
        tab_p[44].setPosition_x(395);
        tab_p[44].setPosition_y(706);
        tab_p[45].setPosition_x(336);
        tab_p[45].setPosition_y(706);
        tab_p[46].setPosition_x(277);
        tab_p[46].setPosition_y(706);
        tab_p[47].setPosition_x(277);
        tab_p[47].setPosition_y(669);
        tab_p[48].setPosition_x(277);
        tab_p[48].setPosition_y(632);
        tab_p[49].setPosition_x(277);
        tab_p[49].setPosition_y(595);
        tab_p[50].setPosition_x(277);
        tab_p[50].setPosition_y(558);
        tab_p[51].setPosition_x(218);
        tab_p[51].setPosition_y(558);
        tab_p[52].setPosition_x(159);
        tab_p[52].setPosition_y(558);
        tab_p[53].setPosition_x(100);
        tab_p[53].setPosition_y(558);
        tab_p[54].setPosition_x(41);
        tab_p[54].setPosition_y(558);
        tab_p[55].setPosition_x(41);
        tab_p[55].setPosition_y(521);
        tab_p[56].setPosition_x(100);
        tab_p[56].setPosition_y(521);
        tab_p[57].setPosition_x(159);
        tab_p[57].setPosition_y(521);
        tab_p[58].setPosition_x(218);
        tab_p[58].setPosition_y(521);
        tab_p[59].setPosition_x(277);
        tab_p[59].setPosition_y(521);
        tab_p[60].setPosition_x(336);
        tab_p[60].setPosition_y(373);
        tab_p[61].setPosition_x(336);
        tab_p[61].setPosition_y(410);
        tab_p[62].setPosition_x(336);
        tab_p[62].setPosition_y(447);
        tab_p[63].setPosition_x(336);
        tab_p[63].setPosition_y(484);
        tab_p[64].setPosition_x(336);
        tab_p[64].setPosition_y(669);
        tab_p[65].setPosition_x(336);
        tab_p[65].setPosition_y(632);
        tab_p[66].setPosition_x(336);
        tab_p[66].setPosition_y(595);
        tab_p[67].setPosition_x(336);
        tab_p[67].setPosition_y(558);
        tab_p[68].setPosition_x(572);
        tab_p[68].setPosition_y(521);
        tab_p[69].setPosition_x(513);
        tab_p[69].setPosition_y(521);
        tab_p[70].setPosition_x(454);
        tab_p[70].setPosition_y(521);
        tab_p[71].setPosition_x(395);
        tab_p[71].setPosition_y(521);
    }
}