import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_SendPlayerKostka extends Thread {

    private final ObjectOutputStream dos;
    private final int a;
    private final Socket s;

    public Client_SendPlayerKostka(Socket s, ObjectOutputStream dos, int a) {
        this.s = s;
        this.dos = dos;
        this.a = a;
    }

    /* Watek odpowiedzialny za nadanie graczowi numer */
    public void run() {
        try {
            dos.writeObject(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}