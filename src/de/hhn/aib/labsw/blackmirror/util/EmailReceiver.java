package de.hhn.aib.labsw.blackmirror.util;

import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import java.util.*;

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
    public EmailInfo checkForMails() throws MessagingException {
        if (imapStore == null) {
            throw new IllegalStateException(resources.getString("failedToConnect"));
        }

        // Folder name might be dependent on mail provider "INBOX" works on most
        Folder mailFolder = imapStore.getFolder("INBOX");
        mailFolder.open(Folder.READ_ONLY);

        HashMap<Integer, String> mailSubjects = new HashMap<>();
        HashMap<Integer, String> mailSenderAddresses = new HashMap<>();

        int messageCount = mailFolder.getMessageCount();

        boolean firstIndexIsNewest = mailFolder.getMessage(messageCount).getReceivedDate().before(mailFolder.getMessage(1).getReceivedDate());

        int j = 0;
        if(firstIndexIsNewest) {
            for (int i = 3; i >= 1; i--) {
                j = extractMailInfo(mailFolder, mailSubjects, mailSenderAddresses, j, i);
            }
        } else {
            for (int i = messageCount - 2; i <= messageCount; i++) {
                j = extractMailInfo(mailFolder, mailSubjects, mailSenderAddresses, j, i);
            }
        }

        mailFolder.close();
        imapStore.close();

        return new EmailInfo(mailFolder.getUnreadMessageCount(), mailSenderAddresses, mailSubjects);
    }

    private int extractMailInfo(Folder mailFolder, HashMap<Integer, String> mailSubjects, HashMap<Integer, String> mailSenderAddresses, int j, int i) throws MessagingException {
        // only extract name of sender not the address
        int endOfSubStr = mailFolder.getMessage(i).getFrom()[0].toString().indexOf("<");
        if (endOfSubStr != -1) {
            mailSenderAddresses.put(j, mailFolder.getMessage(i).getFrom()[0].toString().substring(0, endOfSubStr - 1));
        } else {
            mailSenderAddresses.put(j, mailFolder.getMessage(i).getFrom()[0].toString());
        }

        mailSubjects.put(j, mailFolder.getMessage(i).getSubject());
        j++;
        return j;
    }
}
