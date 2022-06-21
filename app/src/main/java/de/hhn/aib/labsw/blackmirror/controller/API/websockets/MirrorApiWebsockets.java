package de.hhn.aib.labsw.blackmirror.controller.API.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApi;
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * MirrorApiServer provides an easy to use Interface to the App.
 * Author: Luis Gutzeit
 * Version: 1.1 - 19.04.2022
 */
public class MirrorApiWebsockets extends WebSocketServer implements MirrorApi {
    private static final int PORT=2306;

    //https://mvnrepository.com/artifact/org.java-websocket/Java-WebSocket/1.5.3
    //contains all the sessions active at the moment
    List<WebSocket> sessions = new ArrayList<>();

    //Used to translate Json<->POJO
    ObjectMapper mapper = new ObjectMapper();

    //maps listerners to their corresponding topics
    Map<String, List<TopicListener>> listeners = new HashMap<>();

    //used for singleton
    private static MirrorApiWebsockets instance = null;

    /**
     * initialise the server
     */
    public void init(){
        instance.start();
    }

    /**
     * stop the server
     */
    public void finish(){
        try {
            instance.stop();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * private constructor (singleton pattern)
     * singleton pattern is necessary because multiple instances could conflict with each other
     * SINGLETON PATTERN MAY CHANGE IN THE FUTURE
     */
    private MirrorApiWebsockets(){
        super(new InetSocketAddress(PORT));
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
            if(message.equals("alive?")){
                session.send("yes!");
            }
            else {
                //try to parse the string into JSON
                JsonNode jsonNode = mapper.readTree(message);
                String topic = jsonNode.get("topic").textValue();
                if (jsonNode.get("payload") == null) {
                    throw new IllegalArgumentException("wrong json format");
                }

                //notify each listener for the topic if the JSON is valid
                List<TopicListener> listenersList = listeners.get(topic);
                if (listenersList != null) {
                    //Iterater is being used to delete dead references
                    Iterator<TopicListener> topicIterator = listenersList.iterator();
                    while (topicIterator.hasNext()) {
                        TopicListener element = topicIterator.next();
                        try {
                            element.dataReceived(topic, jsonNode.get("payload"));
                        } catch (NullPointerException e) {
                            topicIterator.remove();
                        }
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
        System.out.println(this.getAddress());
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
     * unsubscribe to updates on a specific topic. Subscriptions to other topics remain active
     * @param topic the topic you want to unsubscribe to.
     * @param listener the object that wants to unsubscribe. In most cases this will probably be "this"
     */
    public void unsubscribe(String topic, TopicListener listener){
        List<TopicListener> listenerList = listeners.get(topic);
        if(listenerList != null){
            listenerList.remove(listener);
        }
    }

    /**
     * send a message to the app
     * @param topic the topic of the message
     * @param payload payload of the message as object that will be converted to Json
     */
    public void publish(String topic, Object payload) {
        SendPackage sendPackage = new SendPackage(topic,mapper.valueToTree(payload));
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
        SendPackage sendPackage = new SendPackage(topic,mapper.valueToTree(payload));
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
