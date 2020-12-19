import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.ScreenshotException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimeType;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.regex.Pattern;


public class sendmail {

        public static void sendmail(String wiadomosc, String maile, String attachment) {

            // Create object of Property file
            Properties props = new Properties();

            // this will set host of server- you can change based on your requirement
            props.put("mail.smtp.host", "smtp.gmail.com");

            // set the port of socket factory
            props.put("mail.smtp.socketFactory.port", "587");

            // set socket factory
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

            // set the authentication to true
            props.put("mail.smtp.auth", "true");

            // set the port of SMTP server
            props.put("mail.smtp.port", "587");

            // This will handle the complete authentication
            Session session = Session.getDefaultInstance(props,

                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication("testsendmail321@gmail.com", "sendmail321");

                        }

                    });

            try {


                // Create object of MimeMessage class
                MimeMessage message = new MimeMessage(session);
                message.setHeader("Content-Type", "text/html");
                message.setFrom(new InternetAddress("testsendmail321@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(maile));
                message.setSubject("Momondo loty");

                BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(wiadomosc);

                Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                    messageBodyPart = new MimeBodyPart();
                   // messageBodyPart.setText(wiadomosc);
                    //messageBodyPart.setContent(wiadomosc, "text/html; charset=utf-8");
                    // Create object of MimeMultipart class

                String file = attachment;
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                    String[] tmp = file.split(Pattern.quote("\\"));
                    file = tmp[tmp.length-1];
                messageBodyPart.setFileName(file);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

                //message.setContent(multipart);


                // set the content


                // finally send the email
                Transport.send(message);

                System.out.println("=====Email Sent=====");

            } catch (MessagingException e) {

                throw new RuntimeException(e);

            }

        }

}
