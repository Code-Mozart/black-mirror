package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ServerEndpoint(value = "/MirorrAPI")
public class MirrorApiServer {
    static ArrayList<Session> sessions = new ArrayList<>();
    static ObjectMapper mapper = new ObjectMapper();
    static HashMap<String, ArrayList<subscriberItem>> listeners;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        ObjectNode jsonNode = mapper.valueToTree(message);
        String topic = jsonNode.get("topic").asText();
        ArrayList<ApiListener> listenersList = listeners.get(topic);
        if(listenersList != null){
            listenersList.forEach(element->{
                try {
                    mapper.treeToValue(jsonNode,TestClass.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public static <T> void subscribe(String topic, ApiListener<T> listener){
        ArrayList<subscriberItem> listenerList = listeners.get(topic);
        if(listenerList == null){
            listenerList = new ArrayList<>();
            subscriberItem item = new subscriberItem();
            item.listener = listener;
            item.listenerClass = T;
            listenerList.add(listener);
            listeners.put(topic,listenerList);
        }
        else{
            listenerList.add(listener);
        }
    }

    public static void publish(String topic, Object payload) {
        SendPackage sendPackage = new SendPackage();
        sendPackage.topic = topic;
        sendPackage.payload = mapper.valueToTree(payload);
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(mapper.writeValueAsString(sendPackage));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
    }
    static class subscriberItem{
        ApiListener listener;
        Class listenerClass;
    }
}
