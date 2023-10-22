import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Topic implements Serializable {
    public ArrayList<String> topicNames;
    public ArrayList<Map<String,String>> votes;

    public static boolean checkNameTopic(String topic,Topic topicServer) {

        return !topicServer.topicNames.contains(topic);
    }

}
