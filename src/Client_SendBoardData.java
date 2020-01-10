import java_pliki.Pola;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_SendBoardData extends Thread {

    private final ObjectOutputStream dos;
    private final Pola[] tablica_pol;
    private final Socket s;

    public Client_SendBoardData(Socket s, ObjectOutputStream dos, Pola[] d) {
        this.s = s;
        this.dos = dos;
        this.tablica_pol = d;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
        try {
            for (int i = 0; i < tablica_pol.length; i++){
                dos.writeObject(tablica_pol[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}