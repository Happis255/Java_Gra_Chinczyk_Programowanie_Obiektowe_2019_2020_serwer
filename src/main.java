import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class main {

    class Odbior extends Thread
    {
        Socket sock;
        BufferedReader sockReader;

        public Odbior(Socket sock) throws IOException
        {
            this.sock=sock;
            this.sockReader=new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }

        public void run()
        {

        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(50007);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        AtomicInteger liczba_graczy = new AtomicInteger(1);
        System.out.println("Jest polaczenie: "+ serverSocket);

        while (true){

            final Socket socket = serverSocket.accept();
            Runnable connection = () -> {

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String line = bufferedReader.readLine();
                    System.out.println("Nadeszlo:" + line);

                    while (!line.contains("0")){
                        bufferedWriter.write(liczba_graczy.get());
                        bufferedWriter.flush();
                        liczba_graczy.getAndIncrement();
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            };
            executorService.submit(connection);
        }
    }
}