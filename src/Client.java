import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static String name;
    private static BufferedReader readLocal;


    public static void main(String[] args) throws IOException {
        BufferedWriter localWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            switch (localReader.readLine()) {
                case "login":
                    login();
                    break;
                case "exit":
                    exit();
                    break;
                case "createTopic":
                    createTopic();
                    break;
                case "view(topic)":
                    System.out.println("Введите название раздела");
                    String topic = localReader.readLine();
                    view(topic);
                    break;
                case "view":
                    view();
                    break;
                case "createVote":
                    createVote();
                    break;
                case "vote":
                    vote();
                    break;
                case "delete":
                    delete();
                    break;
                case "help":
                    System.out.println("Список команд:\n" +
                            "login\n" +
                            "exit\n" +
                            "createTopic\n" +
                            "view(topic)\n" +
                            "view\n" +
                            "createVote\n"+
                            "delete\n");
                    break;
                default:
                    System.out.println("Неизвестная команда");
                    break;
            }


        }

    }

    public static void login () throws IOException {
        readLocal = new BufferedReader(new InputStreamReader(System.in));
        clientSocket = new Socket("127.0.0.1", 8000);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        writing("login");
        System.out.println(reader.readLine());
        Client.name = readLocal.readLine();
        writing(name);
        System.out.println(reader.readLine());
    }
    public static void exit() throws IOException {
        clientSocket.close();
        reader.close();
        writer.close();
        readLocal.close();
    }
    public static void createTopic() throws IOException {
        writing("create topic");
        System.out.println(reader.readLine());
        String topic = readLocal.readLine();
        writing(topic);
        System.out.println(reader.readLine());
    }
    public static void view() throws IOException {
        writing("view");
        int count = Integer.parseInt(reader.readLine());
        for(int i = 0; i < count; i++) {
            System.out.println(reader.readLine());
        }
    }
    public static void view(String topic) throws IOException {
        writing("view topic");
        writing(topic);
        writing(topic);
        while(reader.readLine() != null){

        }
    }
    public static void createVote() throws IOException {
        writing("createVote");
        System.out.println(reader.readLine());//какой раздел?
        String topic = readLocal.readLine();
        writing(topic);
        String answerByTopic = reader.readLine();//
        if (answerByTopic.equals("Данный раздел пока не создан")){
            System.out.println(answerByTopic);
        }
        else{
            System.out.println(answerByTopic);//имя опроса
            String str = readLocal.readLine();//
            writing(str);

            System.out.println(reader.readLine());//опишите голосование
            str = readLocal.readLine();
            writing(str);

            System.out.println(reader.readLine());//какое количество
            int sizeVote = Integer.parseInt(readLocal.readLine());
            writing(String.valueOf(sizeVote));

            System.out.println(reader.readLine());//введите варанты
            for (int i = 0;i<sizeVote;i++){
                str = readLocal.readLine();
                writing(str);
            }
        }
    }
    public static void vote() throws IOException {
        writing("vote");
        System.out.println(reader.readLine());//topic?
        String topic = readLocal.readLine();
        writing(topic);

        System.out.println(reader.readLine());//vote?
        String vote = readLocal.readLine();
        writing(vote);

        System.out.println(reader.readLine());//пункт
        while (!reader.readLine().equals("exit")){
            String str = reader.readLine();
        }
        System.out.println(reader.readLine());
        String str = readLocal.readLine();
        writing(str);


    }
    public static void delete() throws IOException {
        System.out.println(reader.readLine());
        String topic = readLocal.readLine();
        writing(topic);

        System.out.println(reader.readLine());
        String vote = readLocal.readLine();
        writing(vote);
        System.out.println(reader.readLine());
    }
    public static void writing(String text) throws IOException {
        writer.write(text+"\n");
        writer.flush();

    }

}
