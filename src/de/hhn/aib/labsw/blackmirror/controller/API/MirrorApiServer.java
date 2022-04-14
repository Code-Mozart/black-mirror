package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MirrorApiServer extends WebSocketServer {
    ArrayList<WebSocket> sessions = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, ArrayList<ApiListener>> listeners = new HashMap<>();
    private static MirrorApiServer instance = null;

    private MirrorApiServer(){
        instance = this;
    }

    public static MirrorApiServer getInstance(){
        if(instance == null){
            instance = new MirrorApiServer();
        }
        return instance;
    }

    @Override
    public void onOpen(WebSocket session,ClientHandshake clientHandshake) {
        // Get session and WebSocket connection
        sessions.add(session);
        System.out.print("new Connection!: ");
        System.out.println(session.getRemoteSocketAddress().getAddress().toString());

        TestClass t = new TestClass();
        t.lat = 14.1241;
        t.lon = 2.2112;
        this.publish("location",t);
    }

    @Override
    public void onMessage(WebSocket session, String message) {
        // Handle new messages
        System.out.println(message);
        try {
            JsonNode jsonNode = mapper.readTree(message);
            String topic = jsonNode.get("topic").textValue();
            if(jsonNode.get("payload") == null){
                throw new IllegalArgumentException("wrong json format");
            }
            ArrayList<ApiListener> listenersList = listeners.get(topic);
            if(listenersList != null){
                listenersList.forEach(element->{
                    element.dataReceived(topic, jsonNode);
                });
            }
            System.out.println(mapper.treeToValue(jsonNode.get("payload"),TestClass.class));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onClose(WebSocket session, int code, String reason, boolean remote) {
        // WebSocket connection closes
        sessions.remove(session);
    }

    @Override
    public void onError(WebSocket session, Exception ex) {
        // Do error handling here
    }

    @Override
    public void onStart() {
        System.out.println("Server started");
    }

    public void subscribe(String topic, ApiListener listener){
        ArrayList<ApiListener> listenerList = listeners.get(topic);
        if(listenerList == null){
            listenerList = new ArrayList<>();
            listenerList.add(listener);
            listeners.put(topic,listenerList);
        }
        else{
            listenerList.add(listener);
        }
    }

    public void publish(String topic, Object payload) {
        SendPackage sendPackage = new SendPackage();
        sendPackage.topic = topic;
        sendPackage.payload = mapper.valueToTree(payload);
        sessions.forEach(session -> {
            try {
                session.send(mapper.writeValueAsString(sendPackage));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
    }

    public void publish(String topic, JsonNode payload) {
        SendPackage sendPackage = new SendPackage();
        sendPackage.topic = topic;
        sendPackage.payload = payload;
        sessions.forEach(session -> {
            try {
                session.send(mapper.writeValueAsString(sendPackage));
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        });
    }
}
