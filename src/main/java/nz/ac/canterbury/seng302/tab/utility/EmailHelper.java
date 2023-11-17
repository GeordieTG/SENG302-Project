package nz.ac.canterbury.seng302.tab.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Handles email sending functionality
 */
@Component
public class EmailHelper {

    /**
     * Allows us to access email sending functionality with Spring-Boot-Starter-Email
     */
    @Autowired
    private JavaMailSender emailSender;

    /**
     * Determines the url to use depending on the environment the
     * website is being run on (local/test/prod)
     *
     * @param url the url of the calling page obtained through javascript
     * @return the base of the required url
     */
    public static String getUrl(String url) {
        String emailLink;
        if (url.contains("//localhost")) {
            emailLink = "http://localhost:8080/";
        } else if (url.contains("csse-s302g4.canterbury.ac.nz/test")) {
            emailLink = "https://csse-s302g4.canterbury.ac.nz/test/";
        } else if (url.contains("csse-s302g4.canterbury.ac.nz/prod")) {
            emailLink = "https://csse-s302g4.canterbury.ac.nz/prod/";
        } else {
            // If the link cant be found, Easter egg of our application
            emailLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        }
        return emailLink;
    }

    /**
     * Functionality to send an email. Properties for the account and email
     * sending are stored in application.properties
     *
     * @param to      Email address to send to as a String
     * @param subject Subject of the email as a String
     * @param body    Body of the email as a String
     */
    public void sendmail(String to, String subject, String body) {

        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("team400seng302@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
            Thread.currentThread().interrupt();
        }).start();
    }

}
