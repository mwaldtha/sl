package sl.interview.communication;

import sl.interview.PropertyLoader;
import sl.interview.exceptions.ConfigurationException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * SMTP implementation of the Messenger interface, for sending email messages.
 */
public class SMTPMessenger implements Messenger {

    /**
     * Sends an email message using values retrieved from the default configuration properties file or the file
     * specified in the mail_props_file system property.
     *
     * @throws MessagingException
     * @throws ConfigurationException
     */
    public void send() throws MessagingException, ConfigurationException {
        Properties mailProps = PropertyLoader.load(System.getProperty("mail_props_file","/email-config.properties"));

        Session mailSession = Session.getInstance(mailProps);

        MimeMessage mailMessage = new MimeMessage(mailSession);

        String fromVal = mailProps.getProperty("mail.from");
        if (fromVal == null || fromVal.isEmpty()) {
            throw new ConfigurationException("No 'from' address was found, unable to send message.");
        } else {
            mailMessage.setFrom(new InternetAddress(mailProps.getProperty("mail.from")));
        }

        String toVal = mailProps.getProperty("mail.to");
        if (toVal == null || toVal.isEmpty()) {
            throw new ConfigurationException("No 'to' address was found, unable to send message.");
        } else {
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailProps.getProperty("mail.to")));
        }

        mailMessage.setSubject(mailProps.getProperty("mail.subject"));
        mailMessage.setText(mailProps.getProperty("mail.text"));

        System.out.printf("Sending email to %s.%n", mailProps.getProperty("mail.to"));

        Transport.send(mailMessage);
    }
}
