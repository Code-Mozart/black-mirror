package de.hhn.aib.labsw.blackmirror.controller.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class TestClass implements TopicListener{
    MirrorApiServer server;
    String topic;

    public TestClass(String topic){
        server = MirrorApiServer.getInstance();
        server.subscribe(topic,this);
        this.topic = topic;
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        System.out.print(topic);
        System.out.println(": new Dataset arrived!");
        try {
            System.out.println(server.getMapper().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
