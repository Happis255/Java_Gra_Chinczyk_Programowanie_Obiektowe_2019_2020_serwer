import java_pliki.Gracz;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_SendPlayersData extends Thread {

    private final ObjectOutputStream dos;
    private final Socket s;
    private final Gracz[] tablica_graczy;

    public Client_SendPlayersData(Socket s, ObjectOutputStream dos, Gracz[] d) {
        this.s = s;
        this.dos = dos;
        this.tablica_graczy = d;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
            for (int i = 0; i < 4; i++){
                try {
                    dos.writeObject(tablica_graczy[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}