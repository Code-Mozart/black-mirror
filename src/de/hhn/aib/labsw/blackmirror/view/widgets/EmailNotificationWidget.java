package de.hhn.aib.labsw.blackmirror.view.widgets;

import de.hhn.aib.labsw.blackmirror.util.EmailInfo;
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
    private final JLabel numOfMailsLabel;
    private final JLabel mail1;
    private final JLabel mail2;
    private final JLabel mail3;

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

        panel.setLayout(new GridLayout(5, 1));

        JLabel iconLabel = new JLabel("", SwingConstants.CENTER);
        // icon author "Vecteezy.com"
        ImageIcon mailIcon = new ImageIcon("icons/mail.png");
        iconLabel.setIcon(mailIcon);

        numOfMailsLabel = new JLabel("", SwingConstants.CENTER);
        numOfMailsLabel.setForeground(Color.WHITE);
        numOfMailsLabel.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));

        mail1 = new JLabel("", SwingConstants.CENTER);
        mail1.setForeground(Color.WHITE);

        mail2 = new JLabel("", SwingConstants.CENTER);
        mail2.setForeground(Color.WHITE);

        mail3 = new JLabel("", SwingConstants.CENTER);
        mail3.setForeground(Color.WHITE);

        // todo: panel layout arrangement
        panel.add(iconLabel);
        panel.add(numOfMailsLabel);
        // mail 3 is the newest
        panel.add(mail3);
        panel.add(mail2);
        panel.add(mail1);

        this.add(panel);

        receiver = new EmailReceiver();

        // todo: user data should later be obtained from db
        // hotmail imap server: imap-mail.outlook.com
        // web.de imap server: imap.web.de
        // gmail.com imap server: imap.gmail.com
        // Project Mail: "blackmirror.labswp@gmail.com", "labSWPproject" (error: app not a "secure app" for google)
        // "blackmirror.labswp@web.de", "labSWPproject"
        host = "imap.web.de";
        port = "993";
        username = "blackmirror.labswp@web.de";
        password = "labSWPproject";

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
            EmailInfo emailInfo = receiver.checkForMails();

            int numOfUnreadMails = emailInfo.getNumOfUnreadMail();
            String newMsgs;
            if (numOfUnreadMails == 0) {
                newMsgs = resources.getString("noNewMails");
            } else if (numOfUnreadMails == 1) {
                newMsgs = resources.getString("oneNewMail");
            } else {
                newMsgs = resources.getString("youHave") + " " + numOfUnreadMails + " " + resources.getString("newMails");
            }

            numOfMailsLabel.setText(newMsgs);

            if(numOfUnreadMails == 3) {
                mail3.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
                mail2.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
                mail1.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
            } else if(numOfUnreadMails == 2) {
                mail3.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
                mail2.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
                mail1.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
            } else if(numOfUnreadMails == 1) {
                mail3.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.BOLD, 16));
                mail2.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
                mail1.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
            } else {
                mail3.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
                mail2.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
                mail1.setFont(new Font(numOfMailsLabel.getFont().getFontName(), Font.PLAIN, 14));
            }

            mail3.setText(emailInfo.getMailSenderAddresses().get(2) + " : " + emailInfo.getMailSubjects().get(2));
            mail2.setText(emailInfo.getMailSenderAddresses().get(1) + " : " + emailInfo.getMailSubjects().get(1));
            mail1.setText(emailInfo.getMailSenderAddresses().get(0) + " : " + emailInfo.getMailSubjects().get(0));


        } catch (MessagingException me) {
            me.printStackTrace();
            numOfMailsLabel.setText(resources.getString("failedToConnect"));
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
