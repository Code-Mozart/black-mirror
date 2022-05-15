package de.hhn.aib.labsw.blackmirror.controller.widgets;

import com.sun.mail.imap.IMAPStore;
import de.hhn.aib.labsw.blackmirror.view.widgets.AbstractWidget;
import de.hhn.aib.labsw.blackmirror.view.widgets.EmailNotificationWidget;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controller class for {@link EmailNotificationWidget}.
 *
 * @author Philipp Herda
 * @author Markus Marewitz
 * @version 2022-05-15
 */
public class EmailNotificationController extends AbstractWidgetController {

    protected IMAPStore imapStore = null;
    private final ResourceBundle resources = ResourceBundle.getBundle("lang/EmailNotificationWidget", Locale.getDefault());

    private final EmailNotificationWidget widget;

    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public EmailNotificationController() {
        widget = new EmailNotificationWidget();

        // todo: user data should later be obtained from db
        // hotmail imap server: imap-mail.outlook.com
        // web.de imap server: imap.web.de
        // gmail.com imap server: imap.gmail.com
        // Project Mail: "blackmirror.labswp@gmail.com", "labSWPproject" (error: app not a "secure app" for google)
        host = "imap.web.de";
        port = "993";
        username = "blackmirror.labswp@web.de";
        password = "labSWPproject";

        update();
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
    public void login(String host, String port, String username, String password) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", port);
        properties.put("mail.imaps.starttls.enable", "true");

        Session mailSession = Session.getDefaultInstance(properties);

        Store store = mailSession.getStore("imaps");
        store.connect(host, username, password);

        this.imapStore = (IMAPStore) store;
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
            login(host, port, username, password);
            int count = getUnreadMessagesCount();
            widget.drawUnreadEmails(count);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
