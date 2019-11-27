import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client_GiveNumber extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final int numer_gracza;

    public Client_GiveNumber(Socket s, DataInputStream dis, DataOutputStream dos, int i) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.numer_gracza = i;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
        try {
            dos.writeInt(this.numer_gracza);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
