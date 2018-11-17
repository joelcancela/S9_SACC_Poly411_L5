package fr.unice.polytech.si5.cc.l5.model;

/**
 * Class Email
 *
 * This class is used when deserializing the JSON Object sent to the Email Servlet
 *
 * @author Nikita ROUSSEAU
 */
public class Email {
    private String to;
    private String to_meta;
    private String subject;
    private String body;

    public Email() {
        this.to = "";
        this.to_meta = "";
        this.subject = "";
        this.body = "";
    }

    public Email(String to, String to_meta, String subject, String body) {
        this.to = to;
        this.to_meta = to_meta;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo_meta() {
        return to_meta;
    }

    public void setTo_meta(String to_meta) {
        this.to_meta = to_meta;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
