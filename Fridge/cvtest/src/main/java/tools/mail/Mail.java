package tools.mail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
public class Mail {
    private byte[] imgToByteArray(BufferedImage img){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
    public void sendMail(BufferedImage img, BufferedImage img2, String barcode) throws MessagingException {

        System.out.println("Sending mail...");
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.mailtrap.io");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.enable","true");

        props.setProperty("mail.user", "bb1f0ec3e14221");
        props.setProperty("mail.password", "a159242a9ffce3");

        //props.setProperty("mail.smtp.auth","true");

        Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bb1f0ec3e14221", "a159242a9ffce3");
            }
        });
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("New product Request");
        message.setFrom(new InternetAddress("me@sender.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("you@receiver.com"));

        //
        // This HTML mail have to 2 part, the BODY and the embedded image
        //
        MimeMultipart multipart = new MimeMultipart("related");

        // first part  (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>New product requested</H1><b>"+barcode+"</b><img src=\"cid:image\"><img src=\"cid:image2\">";
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataHandler dataHandler=new DataHandler(imgToByteArray(img),"image/png");
        messageBodyPart.setDataHandler(dataHandler);
        messageBodyPart.setHeader("Content-ID","<image>");

        // add it
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataHandler dataHandler2=new DataHandler(imgToByteArray(img),"image/png");
        messageBodyPart.setDataHandler(dataHandler2);
        messageBodyPart.setHeader("Content-ID","<image2>");

        // add it
        multipart.addBodyPart(messageBodyPart);
        // put everything together
        message.setContent(multipart);

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}

