package de.hhn.aib.labsw.blackmirror.widgets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.hhn.aib.labsw.blackmirror.controller.widgets.EmailNotificationController;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.EmailLoginData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

class EmailNotificationControllerTest {

    private EmailNotificationController controller;
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new ObjectMapper();
        controller = new EmailNotificationController();
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedValid() {
        Assertions.assertDoesNotThrow(() -> controller.getLoginDataFromJSON(
                createLoginData("host", 1234, "username", "password")),
                "method throws although login data is correct");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedInvalid() {
        Assertions.assertDoesNotThrow(() -> controller.dataReceived(
                        "emailData", createLoginData(null, -1, "", null)),
                "dataReceived() method should handle IllegalArgumentException");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedPortValid() {
        Assertions.assertDoesNotThrow(() -> controller.getLoginDataFromJSON(createLoginData(
                        "host", 0, "username", "password")),
                "port number should be allowed to be 0");
        Assertions.assertDoesNotThrow(() -> controller.getLoginDataFromJSON(createLoginData(
                        "host", 0xFFFF, "username", "password")),
                "port number should be allowed to be 65535");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedInvalidPort() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("host", 0xFFFF + 1, "username", "password")),
                "port numbers greater than 65535 should not be allowed");

        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("host", -1, "username", "password")),
                "negative port numbers should not be allowed");

        ObjectNode json = (ObjectNode) createLoginData("host", 0xFFFF + 1, "username", "password");
        json.putNull("port");
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        json),
                "port number must not be null");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedInvalidHost() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData(null, 1234, "username", "password")),
                "host must not be null");
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("", 1234, "username", "password")),
                "host must not be empty");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedInvalidUsername() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("host", 1234, null, "password")),
                "username must not be null");
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("host", 1234, "", "password")),
                "username must not be empty");
    }

    @org.junit.jupiter.api.Test
    void testEmailLoginDataReceivedInvalidPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> controller.getLoginDataFromJSON(
                        createLoginData("host", 1234, "username", null)),
                "password must not be null");
    }

    private JsonNode createLoginData(String host, int port, String username, String password) {
        return mapper.valueToTree(new EmailLoginData(host, port, username, password));
    }
}
