package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.util.EmailReceiver;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Herda
 * @version 2022-03-24
 */
public class EmailNotificationWidget extends AbstractWidget {

    private final EmailReceiver receiver;
    private final JLabel textLabel;

    private final String host;
    private final String port;
    private final String username;
    private final String password;

    private int counter = 0;

    public EmailNotificationWidget() {
        this.setSize(400, 300);
        this.setLocation(500, 300);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        panel.setLayout(new GridLayout(2, 1));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        // icon author "Vecteezy.com"
        ImageIcon mailIcon = new ImageIcon("icons/mail.png");
        iconLabel.setIcon(mailIcon);

        textLabel = new JLabel("", SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font(textLabel.getFont().getFontName(), Font.BOLD, 20));

        panel.add(iconLabel);
        panel.add(textLabel);
        this.add(panel);

        receiver = new EmailReceiver();

        // user data should later be obtained from db
        // hotmail imap server: imap-mail.outlook.com
        // web.de imap server: imap.web.de
        // gmail.com imap server: imap.gmail.com
        // Project Mail: "blackmirror.labswp@gmail.com", "labSWPproject" (error: app not a "secure app" for google)
        host = "imap.web.de";
        port = "993";
        username = "yourMailAdressHere";
        password = "yourPwHere";

        try {
            drawUnreadEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the number of unread mails on the panel
     *
     * @throws MessagingException if connection failed
     */
    private void drawUnreadEmails() throws MessagingException {
        receiver.login(host, port, username, password);
        textLabel.setText(receiver.checkForMails());
    }

    /**
     * Check every 10 seconds for new mail
     */
    @Override
    public void onNextSecond() {
        counter += 1;
        if (counter % 10 == 0) {
            try {
                drawUnreadEmails();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            counter = 0;
        }
    }
}
