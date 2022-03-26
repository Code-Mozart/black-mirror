package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.utility.EmailReceiver;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Herda
 * @version 2022-03-24
 */
public class EmailNotificationWidget extends AbstractWidget {

    private EmailReceiver receiver;
    private JLabel label;

    private int counter = 0;

    public EmailNotificationWidget() {
        this.setSize(400, 400);
        this.setLocation(400,400);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        label = new JLabel("", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(label.getFont().getFontName(),Font.BOLD, 16));

        panel.add(label);
        this.add(panel);

        receiver = new EmailReceiver();

        // todo: get login data from resource/(DB?)
        try {
            // hotmail imap server: imap-mail.outlook.com
            // web.de imap server: imap.web.de
            receiver.login("imap.web.de", "993", "user@web.de", "userPWhere!");
            receiver.checkForMails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check ever 10 seconds for new mail
     */
    @Override
    public void onNextSecond() {
        counter += 1;
        if(counter % 10 == 0) {
            try {
                drawUnreadEmails();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            counter = 0;
        }
    }

    /**
     * Displays the number of unread mails on the panel
     * @throws MessagingException   if connection failed
     */
    private void drawUnreadEmails() throws MessagingException {
        label.setText("");
        receiver.login("imap.web.de", "993", "username", "password");
        label.setText(receiver.checkForMails());
    }
}
