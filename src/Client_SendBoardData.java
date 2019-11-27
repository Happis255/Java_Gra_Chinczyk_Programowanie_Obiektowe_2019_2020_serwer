import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_SendBoardData extends Thread {

    private final ObjectOutputStream dos;
    private final Pola[] tablica_pol;

    public Client_SendBoardData(ObjectOutputStream dos, Pola[] d) {
        this.dos = dos;
        this.tablica_pol = d;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
        try {
            for (int i = 0; i < 72; i++)
                dos.writeObject(this.tablica_pol[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}