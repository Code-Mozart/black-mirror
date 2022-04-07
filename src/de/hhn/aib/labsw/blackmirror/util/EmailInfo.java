package de.hhn.aib.labsw.blackmirror.util;

import java.util.HashMap;

/**
 * Utility class holding information about the last 3 Emails from the inbox
 * @author Philipp Herda
 * @version 2022-04-07
 */
public class EmailInfo {

    private final int numOfUnreadMail;
    private final HashMap<Integer, String> mailSubjects;
    private final HashMap<Integer, String> mailSenderAddresses;

    public EmailInfo(int numOfUnreadMail, HashMap<Integer, String> mailSenderAddresses, HashMap<Integer, String> mailSubjects) {
        this.numOfUnreadMail = numOfUnreadMail;
        this.mailSenderAddresses = mailSenderAddresses;
        this.mailSubjects = mailSubjects;
    }

    public int getNumOfUnreadMail() {
        return numOfUnreadMail;
    }

    public HashMap<Integer, String> getMailSubjects() {
        return mailSubjects;
    }

    public HashMap<Integer, String> getMailSenderAddresses() {
        return mailSenderAddresses;
    }
}
