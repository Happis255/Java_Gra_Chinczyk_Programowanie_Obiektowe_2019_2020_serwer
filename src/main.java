import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class main {

    /* Czysci terminal odrazu */
    public static void m_fast_clean(){

        for (int i = 0; i < 50; i++) System.out.println("");
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

        ObjectOutputStream tab_2[] = new ObjectOutputStream[liczba_graczy];
        /* Rozeslij wszystkim pola do graczy */
        for (int i = 0; i < liczba_graczy; i++) {

            //ObjectOutputStream dos_object = new ObjectOutputStream (tab_s[i].getOutputStream());

            /* Uruchom watek dla poszczegolnych graczy */
            tab_2[i] = new ObjectOutputStream (tab_s[i].getOutputStream());
            Thread t = new Client_SendBoardData(tab_s[i], tab_2[i], tab_p);
            t.start();
        }
    }
}