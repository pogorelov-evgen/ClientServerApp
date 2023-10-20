import java.io.*;
import java.net.*;


public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8000)){

            while (true) {
                new Thread(()->{

                try(Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {


                    System.out.println("successful");
                    String request = reader.readLine();
                    System.out.println(request);
                    writer.write("The length of your string: " + request.length() + "\n");
                    writer.flush();
                }
                catch (IOException e){
                    throw new RuntimeException(e);
                }
                }).start();
                }

            }
        catch (IOException e){        }
    }
}
