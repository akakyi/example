package tsp.utils;

import tsp.rest.json.response.TaskResponse;

import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Stateless
public class EMailComponent {

    public void sendImmediateTasks(List<TaskResponse> tasks, String recipientAddres) {
        Message message = this.prepareMessage();

        try {
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientAddres, false)
            );

            message.setSubject("Your tasks is burning!");

            StringBuffer messageText = new StringBuffer("Dont forget about:\n");
            tasks.forEach(
                it -> {
                    messageText.append(it.getName());
                    messageText.append("\n");
                }
            );
            message.setText(messageText.toString());
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException mex) {
            throw new RuntimeException("Message sender is  broken!", mex);
        }
    }

    public void sendSharedTask(String recipientAddres, String userSenderName, TaskResponse task) {
        Message message = this.prepareMessage();

        try {
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientAddres, false)
            );

            message.setSubject("Your have a new task!");

            String messageText = userSenderName + " send you a new task:\n"
                + task.getName();

            message.setText(messageText);
            message.setSentDate(new Date());

            Transport.send(message);
        } catch (MessagingException mex) {
            throw new RuntimeException("Message sender is  broken!", mex);
        }
    }

    private Message prepareMessage() {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = "cthulhulikesbloodyofferings@gmail.com";
        final String password = "";

        Message message;

        try {
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
        } catch (MessagingException mex) {
            throw new RuntimeException("Message sender is  broken!", mex);
        }

        return message;
    }

}
