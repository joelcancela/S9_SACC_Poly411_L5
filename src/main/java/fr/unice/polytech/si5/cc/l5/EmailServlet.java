package fr.unice.polytech.si5.cc.l5;

import com.google.gson.Gson;
import fr.unice.polytech.si5.cc.l5.model.Email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "EmailServlet", value = "/email")
public class EmailServlet extends HttpServlet {

    static String EMAIL_SUFFIX = "polar-winter-218511.appspotmail.com";
    static String EMAIL_META = "NOREPLY @ AppEngine";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        Email email = new Email(
            "<email>@gmail.com",
            "Firstname LASTNAME",
            "Testing Email Service Subject",
            "This is an example of email body"
        );

        resp.setStatus(200); // Bad Request
        resp.getWriter().println(gson.toJson(email));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read input
        BufferedReader reader = req.getReader();
        Email email;

        try {
            email = new Gson().fromJson(reader, Email.class);
        } catch (Exception e) {
            resp.setStatus(400); // Bad Request
            resp.getWriter().println("La syntaxe de la requête est erronée. (" + e.toString() + ")");
            return;
        }

        if (sendMail(email.getTo(), email.getTo_meta(), email.getSubject(), email.getBody())) {
            resp.setStatus(200); // OK
            return;
        }

        resp.setStatus(500); // Internal Server Error
    }

    /**
     * Send Mail w/ AppEngine API
     *
     * @param destination_address Email Address of the Destination
     * @param destination_meta First and Lastname of the Destination
     * @param subject Email Subject field
     * @param body Email contents
     * @return boolean
     */
    private boolean sendMail(
        String destination_address,
        String destination_meta,
        String subject,
        String body) {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noreply@" + EMAIL_SUFFIX, EMAIL_META));

            msg.addRecipient(Message.RecipientType.TO,
                new InternetAddress(destination_address, destination_meta)
            );

            msg.setSubject(subject);

            msg.setText(body);

            Transport.send(msg);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
