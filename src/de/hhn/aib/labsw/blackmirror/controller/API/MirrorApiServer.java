package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.Location;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MirrorApiServer extends WebSocketServer {
    ArrayList<WebSocket> sessions = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, ArrayList<TopicListener>> listeners = new HashMap<>();
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

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public void onOpen(WebSocket session, ClientHandshake clientHandshake) {
        sessions.add(session);
        System.out.print("new Connection!: ");
        System.out.println(session.getRemoteSocketAddress().getAddress().toString());
    }

    @Override
    public void onMessage(WebSocket session, String message) {
        try {
            JsonNode jsonNode = mapper.readTree(message);
            String topic = jsonNode.get("topic").textValue();
            if(jsonNode.get("payload") == null){
                throw new IllegalArgumentException("wrong json format");
            }
            ArrayList<TopicListener> listenersList = listeners.get(topic);
            if(listenersList != null){
                listenersList.forEach(element->{
                    try {
                        element.dataReceived(topic, jsonNode);
                    }
                    catch(NullPointerException e){
                        listenersList.remove(element);
                    }
                });
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onClose(WebSocket session, int code, String reason, boolean remote) {
        // WebSocket connection closes
        System.out.print("Connection closed: ");
        System.out.println(session.getRemoteSocketAddress().getAddress().toString());
        sessions.remove(session);
    }

    @Override
    public void onError(WebSocket session, Exception ex) {
        session.close();
        sessions.remove(session);
    }

    @Override
    public void onStart() {
        System.out.println("Server started");
    }

    public void subscribe(String topic, TopicListener listener){
        ArrayList<TopicListener> listenerList = listeners.get(topic);
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
