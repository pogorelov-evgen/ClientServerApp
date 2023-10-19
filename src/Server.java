import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Collections;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        int count = 0;

        while (true) {

            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());

            String str = reader.readLine();
            System.out.println(str);

            System.out.println("successful accepted: " + (++count));

            writer.write("You "+ count+ " client. Welcome!" + "The length of your string: " + str.length()+"\n");
            writer.flush();


            reader.close();
            writer.close();
            clientSocket.close();

        }

    }
}
