package de.hhn.aib.labsw.blackmirror.util;

import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Philipp Herda
 * @version 2022-04-04
 * Simple utility class to login to a mail provider via imap
 * Ensure a login() call before every checkForMail() call
 */
public class EmailReceiver {

    protected IMAPStore imapStore;
    private final ResourceBundle resources = ResourceBundle.getBundle("EmailNotificationWidget", Locale.getDefault());

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
    public String checkForMails() throws MessagingException {
        if (imapStore == null) {
            throw new IllegalStateException(resources.getString("failedToConnect"));
        }

        // Folder name might be dependent on mail provider "INBOX" works on most
        Folder mailFolder = imapStore.getFolder("INBOX");
        mailFolder.open(Folder.READ_ONLY);

        String newMsgs;
        if (mailFolder.getUnreadMessageCount() == 0) {
            newMsgs = resources.getString("noNewMails");
        } else if (mailFolder.getUnreadMessageCount() == 1) {
            newMsgs = resources.getString("oneNewMail");
        } else {
            newMsgs = resources.getString("youHave") + " " + mailFolder.getUnreadMessageCount() + " " + resources.getString("newMails");
        }

        mailFolder.close();
        imapStore.close();

        return newMsgs;
    }
}
