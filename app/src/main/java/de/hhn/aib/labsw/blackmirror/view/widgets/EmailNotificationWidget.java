package de.hhn.aib.labsw.blackmirror.view.widgets;

import jakarta.mail.*;
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

    private final ResourceBundle resources = ResourceBundle.getBundle("lang/EmailNotificationWidget", Locale.getDefault());
    private final JLabel textLabel;

    public EmailNotificationWidget() {
        //this.setSize(400, 300);
        //this.setLocation(500, 300);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        panel.setLayout(new GridLayout(2, 1));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        // icon author "Vecteezy.com"
        ImageIcon mailIcon = new ImageIcon(getClass().getResource("/icons/mail.png"));
        iconLabel.setIcon(mailIcon);

        textLabel = new JLabel("", SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font(textLabel.getFont().getFontName(), Font.BOLD, 16));

        panel.add(iconLabel);
        panel.add(textLabel);
        this.add(panel);
    }

    /**
     * Displays the number of unread mails on the panel
     *
     * @param count The number of unread messages.
     * @throws MessagingException if connection failed
     */
    public void drawUnreadEmails(int count) throws MessagingException {
        String newMsgs = switch (count) {
            case 0 -> resources.getString("noNewMails");
            case 1 -> resources.getString("oneNewMail");
            default -> resources.getString("youHave") + " " + count + " " + resources.getString("newMails");
        };
        textLabel.setText(newMsgs);
    }

    public void drawLoginFailed() {
        textLabel.setText(resources.getString("failedToConnect"));
    }
}
