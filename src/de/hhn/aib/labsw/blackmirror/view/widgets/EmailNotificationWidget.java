package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.util.EmailReceiver;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This widget displays the number of unread mails in the users inbox
 *
 * @author Philipp Herda
 * @version 2022-04-04
 */
public class EmailNotificationWidget extends AbstractWidget {

    private final EmailReceiver receiver;
    private final ResourceBundle resources = ResourceBundle.getBundle("EmailNotificationWidget", Locale.getDefault());
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
        textLabel.setFont(new Font(textLabel.getFont().getFontName(), Font.BOLD, 16));

        panel.add(iconLabel);
        panel.add(textLabel);
        this.add(panel);

        receiver = new EmailReceiver();

        // todo: user data should later be obtained from db
        // hotmail imap server: imap-mail.outlook.com
        // web.de imap server: imap.web.de
        // gmail.com imap server: imap.gmail.com
        // Project Mail: "blackmirror.labswp@gmail.com", "labSWPproject" (error: app not a "secure app" for google)
        host = "imap.web.de";
        port = "993";
        username = "YOUR_USERNAME_HERE";
        password = "YOUR_PASSWORD_HERE";

        try {
            drawUnreadEmails();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    /**
     * Displays the number of unread mails on the panel
     *
     * @throws MessagingException if connection failed
     */
    private void drawUnreadEmails() throws MessagingException {
        try {
            receiver.login(host, port, username, password);
            textLabel.setText(receiver.checkForMails());
        } catch (MessagingException me) {
            me.printStackTrace();
            textLabel.setText(resources.getString("failedToConnect"));
        }
    }

    /**
     * Checks every 30 seconds for new mail
     */
    @Override
    public void onNextSecond() {
        counter += 1;
        if (counter % 30 == 0) {
            try {
                drawUnreadEmails();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            counter = 0;
        }
    }
}
