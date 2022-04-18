package de.hhn.aib.labsw.blackmirror.controller.API.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.util.*;

/**
 * MirrorApiServer provides an easy to use Interface to the App.
 * Author: Luis Gutzeit
 * Version: 1.0
 */
public class MirrorApiWebsockets extends WebSocketServer implements MirrorApi {
    //https://mvnrepository.com/artifact/org.java-websocket/Java-WebSocket/1.5.3
    List<WebSocket> sessions = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    Map<String, List<TopicListener>> listeners = new HashMap<>();

    private static MirrorApiWebsockets instance = null;

    /**
     * private constructor (singleton pattern)
     * singleton pattern is necessary because multiple instances could conflict with each other
     */
    private MirrorApiWebsockets(){
        instance = this;
    }

    /**
     * get the instance of the server
     * @return the instance of the server
     */
    public static MirrorApiWebsockets getInstance(){
        if(instance == null){
            instance = new MirrorApiWebsockets();
        }
        return instance;
    }

    /**
     * get an instance of an object mapper to convert objects into jsonNodes and vice versa
     * @return the ObjectMapper used in the MirrorApiServer
     */
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
            List<TopicListener> listenersList = listeners.get(topic);
            if(listenersList != null){
                Iterator<TopicListener> topicIterator = listenersList.iterator();
                while(topicIterator.hasNext()) {
                    TopicListener element = topicIterator.next();
                    try {
                        element.dataReceived(topic, jsonNode);
                    } catch (NullPointerException e) {
                        topicIterator.remove();
                    }
                }
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

    /**
     * subscribe to updates on a specific topic. You can subscribe to multiple topics at the same time
     * @param topic the topic you want to subscribe to.
     * @param listener the object that wants to subscribe. In most cases this will probably be "this"
     */
    public void subscribe(String topic, TopicListener listener){
        List<TopicListener> listenerList = listeners.get(topic);
        if(listenerList == null){
            listenerList = new ArrayList<>();
            listenerList.add(listener);
            listeners.put(topic,listenerList);
        }
        else{
            listenerList.add(listener);
        }
    }

    /**
     * send a message to the app
     * @param topic the topic of the message
     * @param payload payload of the message as object that will be converted to Json
     */
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

    /**
     * send a message to the app
     * @param topic the topic of the message
     * @param payload payload of the message as JsonNode
     */
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
