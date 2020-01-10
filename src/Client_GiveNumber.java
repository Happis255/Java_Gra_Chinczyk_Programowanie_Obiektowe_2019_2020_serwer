import java.io.*;
import java.net.Socket;

public class Client_GiveNumber extends Thread {

    final ObjectInputStream dis;
    final ObjectOutputStream dos;
    final Socket s;
    final int numer_gracza;

    public Client_GiveNumber(Socket s, ObjectInputStream dis, ObjectOutputStream dos, int i) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.numer_gracza = i;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
        try {
            dos.writeObject(this.numer_gracza);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
