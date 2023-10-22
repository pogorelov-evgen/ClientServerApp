import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Server {
    private static HashSet<String> names = new HashSet<String>();
    static Logger log;
    static {
        try (FileInputStream ins = new FileInputStream("D:\\programming\\Spring\\ClientServerApp\\src\\log.propertions")) {
            LogManager.getLogManager().readConfiguration(ins);
            log = Logger.getLogger(Server.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("topic.bin"));
        Topic topicServer = (Topic) ois.readObject();
       // topicServer.topicNames = new ArrayList<String>();
        //topicServer.votes = new ArrayList<Map<String,String>>();



        Handler handler = new FileHandler("Log.Server");
        log.setUseParentHandlers(false);
        log.addHandler(handler);

        log.info("server connection");
        try (ServerSocket serverSocket = new ServerSocket(8000)){

            while (true) {
                Thread.sleep(1000);

                log.info("start of multithreading");
                new Thread(()->{

                log.info("client connections");
                try(Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){

                String name = null;
                    while (true) {
                        String request = reader.readLine();
                        switch (request) {
                            case "login":
                                name = login(reader,writer);
                                break;
                            case "create topic":
                                createTopic(reader,writer,topicServer);
                                break;
                            case "view(topic)":
                                String topic = reader.readLine();
                                view(reader,writer,topic,topicServer);
                                break;
                            case "view":
                                view(reader,writer,topicServer);
                                break;
                            case "createVote":
                                createVote(reader,writer,name, topicServer);
                                break;
                            case "vote":
                                vote(reader,writer,topicServer);
                                break;
                            case "delete":
                                delete(reader,writer,name,topicServer);
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
                        }}

                }
                catch (IOException e){
                    throw new RuntimeException(e);
                }
                }).start();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("topic.bin"));
                oos.writeObject(topicServer);
                oos.close();
            }

            }
        catch (IOException | InterruptedException e){        }
    }

    private static void view(BufferedReader reader, BufferedWriter writer, String topic,Topic topics) throws IOException {
        for(Map<String, String> map:topics.votes){
            if(map.get(topic).equals(topic)){
                writing(writer,map.get("name").toString());
            }
        }
    }

    private static void view(BufferedReader reader, BufferedWriter writer, Topic topic) throws IOException {
        writing(writer, String.valueOf(topic.topicNames.size()));
        for(int i = 0;i<topic.topicNames.size();i++){
            writing(writer,topic.topicNames.get(i));
        }
    }

    private static void createTopic(BufferedReader reader, BufferedWriter writer, Topic topicServer) throws IOException {
        writing(writer,"Введите название раздела который вы хотели бы создать");
        String topic = reader.readLine();
        if (Topic.checkNameTopic(topic,topicServer)){
            topicServer.topicNames.add(topic);
            writing(writer,"Раздел с именем "+ topic+" создан");

        }
        else {
            writing(writer,"Раздел с именем "+ topic+" занят другим пользователем");
        }

    }

    private static String login(BufferedReader reader, BufferedWriter writer) throws IOException {
        writing(writer,"Введите ваше имя");
        String name = reader.readLine();
        if (names.contains(name)){
        }
        else {
            names.add(name);
        }
        writing(writer,"Hello! "+name);
        return name;
    }

    private static void writing(BufferedWriter writer, String text) throws IOException {
        writer.write(text+"\n");
        writer.flush();
    }

    private static void createVote(BufferedReader reader, BufferedWriter writer, String name,Topic topicServer) throws IOException {
        writing(writer, "В каком разделе вы хотели бы создать голосование");
        String topic = reader.readLine();
        if (Topic.checkNameTopic(topic,topicServer)){
            writing(writer, "Данный раздел пока не создан");
        } else {
            writing(writer, "Введите имя опроса");

            String str = reader.readLine();
            Map<String, String> map1 = new HashMap<>();
            topicServer.votes.add(map1);
            map1.put("topic", topic);
            map1.put("name", str);
            map1.put("author", name);

            writing(writer, "Опишите ваше голование");
            String description = reader.readLine();
            map1.put("description", description);

            writing(writer, "Какое количество вариантов ответа?");
            int sizeVote = Integer.parseInt(reader.readLine());

            writing(writer, "Введите варианты ответов");
            for (int i = 0; i < sizeVote; i++) {
                str = reader.readLine();
                map1.put((i + 1) + ": " + str, "0");
            }
        }
    }

    private static void vote(BufferedReader reader, BufferedWriter writer, Topic topicServer) throws IOException {
        writing(writer,"В каком разделе вы бы хотели пройти голосование?");
        String topic = reader.readLine();

        writing(writer,"В каком голосовании вы бы хотели поучавствовать?");
        String vote = reader.readLine();

        writing(writer, "Выберите нужный пункт и скопируйте его полностью для ответа");

        for(Map<String, String> map:topicServer.votes){
            if(map.get("name").equals(vote)){
                for(Map.Entry<String, String> entry:map.entrySet()){
                    writing(writer,entry.getKey() + ": " + entry.getValue());
                }
                writing(writer,"exit");
                map.put(reader.readLine(), String.valueOf((Integer.parseInt(map.get(reader.readLine())) + 1)));
            }
        }
        writing(writer,"Ваше голосование пройдено");
    }

    private static void delete(BufferedReader reader, BufferedWriter writer,String name, Topic topicServer) throws IOException {
        writing(writer,"В каком разделе вы бы хотели удалить голосование?");
        String topic = reader.readLine();

        writing(writer,"Какое голосование вы хотели бы удалить?");
        String vote = reader.readLine();

        for(Map<String, String> map:topicServer.votes){
            if(map.get("author").equals(name)){
                topicServer.votes.remove(map);
                writing(writer,"Голосование удалено");
            }
            else writing(writer,"Голосование не найдено, так как вы не являетесь автором");
        }




    }
}
