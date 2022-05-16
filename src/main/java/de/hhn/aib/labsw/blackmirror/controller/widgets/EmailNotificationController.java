package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.mail.imap.IMAPStore;
import de.hhn.aib.labsw.blackmirror.controller.API.websockets.MirrorApiWebsockets;
import de.hhn.aib.labsw.blackmirror.model.ApiDataModels.EmailLoginData;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.EmailNotificationWidget;

import javax.mail.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Controller class for {@link EmailNotificationWidget}.
 *
 * @author Philipp Herda
 * @author Markus Marewitz
 * @version 2022-05-15
 */
public class EmailNotificationController extends AbstractWidgetController {

    public static final String EMAIL_DATA_TOPIC = "emailData";

    protected IMAPStore imapStore = null;
    private final ResourceBundle resources = ResourceBundle.getBundle("lang/EmailNotificationWidget", Locale.getDefault());

    private final EmailNotificationWidget widget;

    private String host;
    private int port;
    private String username;
    private String password;

    public EmailNotificationController() {
        widget = new EmailNotificationWidget();

        // todo: user data should later be obtained from db
        // hotmail imap server: imap-mail.outlook.com
        // web.de imap server: imap.web.de
        // gmail.com imap server: imap.gmail.com
        // Project Mail: "blackmirror.labswp@gmail.com", "labSWPproject" (error: app not a "secure app" for google)
        host = "imap.web.de";
        port = 993;
        username = "blackmirror.labswp@web.de";
        password = "wrongPsw";

        // for test purposes
        // todo: remove
        ScheduledFuture<?> fut = Executors.newSingleThreadScheduledExecutor().schedule(this::testUseNewLoginData, 7, TimeUnit.SECONDS);

        subscribeWithID(EMAIL_DATA_TOPIC);

        update();
    }

    private void testUseNewLoginData() {
        try {
            JsonNode node = MirrorApiWebsockets.getInstance().getMapper().valueToTree(
                    new EmailLoginData("imap.web.de", 993,
                            "blackmirror.labswp@web.de", "labSWPproject")
            );
            dataReceived(EMAIL_DATA_TOPIC, node);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a connection to the mail provider
     *
     * @param host     the hostname of the mail provider
     * @param port     imap port is 993
     * @param username full email address
     * @param password pw for the user
     * @throws MessagingException if connection failed
     */
    public boolean login(String host, int port, String username, String password) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", String.valueOf(port));
        properties.put("mail.imaps.starttls.enable", "true");

        Session mailSession = Session.getDefaultInstance(properties);

        Store store = mailSession.getStore("imaps");
        try {
            store.connect(host, username, password);
        } catch (AuthenticationFailedException e) {
            return false;
        }

        this.imapStore = (IMAPStore) store;

        return true;
    }

    /**
     * Checks the amount of unread messages in the inbox
     * Requires a login() call beforehand
     * Closes connection on it´s own
     * <p>
     * Hint: imap must be manually activated for some mail providers!
     *
     * @return a String containing the amount of new messages
     * @throws MessagingException if connection failed
     */
    private int getUnreadMessagesCount() throws MessagingException {
        if (imapStore == null) {
            throw new IllegalStateException(resources.getString("failedToConnect"));
        }

        // Folder name might be dependent on mail provider "INBOX" works on most
        Folder mailFolder = imapStore.getFolder("INBOX");
        mailFolder.open(Folder.READ_ONLY);

        int count = mailFolder.getUnreadMessageCount();

        mailFolder.close();
        imapStore.close();

        return count;
    }

    private void update() {
        try {
            if (login(host, port, username, password)) {
                int count = getUnreadMessagesCount();
                widget.drawUnreadEmails(count);
            } else {
                widget.drawLoginFailed();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dataReceived(String topic, JsonNode object) {
        assert Objects.equals(topic, EMAIL_DATA_TOPIC);
        try {
            EmailLoginData loginData = getLoginDataFromJSON(object);

            this.host = loginData.host();
            this.port = loginData.port();
            this.username = loginData.username();
            this.password = loginData.password();

            update();
        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public EmailLoginData getLoginDataFromJSON(JsonNode object) throws JsonProcessingException {
        EmailLoginData loginData = nodeToObject(object, EmailLoginData.class);

        if (loginData.host() == null) throw new IllegalArgumentException("host must not be null");
        if (loginData.host().isEmpty()) throw new IllegalArgumentException("host must not be empty");

        if (object.get("port").isNull()) throw new IllegalArgumentException("port must not be null");
        if (loginData.port() < 0 || loginData.port() > 0xFFFF)
            throw new IllegalArgumentException("port must be between 0 and 65535");

        if (loginData.username() == null) throw new IllegalArgumentException("username must not be null");
        if (loginData.username().isEmpty()) throw new IllegalArgumentException("username must not be empty");

        if (loginData.password() == null) throw new IllegalArgumentException("password must not be null");

        return loginData;
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }

    @Override
    public void onRegularUpdate() {
        update();
    }
}
