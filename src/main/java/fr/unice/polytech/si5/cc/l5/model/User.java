package fr.unice.polytech.si5.cc.l5.model;

import com.google.gson.annotations.Expose;

import java.util.OptionalLong;

/**
 * Class User
 *
 * @author Joël CANCELA VAZ
 */
public class User {
    @Expose
    private String email;
    @Expose
    private String name;
    @Expose
    private double score;
    private OptionalLong downloadTimestamp1 = OptionalLong.empty();
    private OptionalLong downloadTimestamp2 = OptionalLong.empty();
    private OptionalLong downloadTimestamp3 = OptionalLong.empty();
    private OptionalLong downloadTimestamp4 = OptionalLong.empty();

    public User() {
        this.downloadTimestamp1 = OptionalLong.empty();
        this.downloadTimestamp2 = OptionalLong.empty();
        this.downloadTimestamp3 = OptionalLong.empty();
        this.downloadTimestamp4 = OptionalLong.empty();
    }

    public User(String email, String name) {
        this();
        this.email = email;
        this.score = 0.0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDownloadTimestamp1() {
        return downloadTimestamp1.orElse(0L);
    }

    public long getDownloadTimestamp2() {
        return downloadTimestamp2.orElse(0L);
    }

    public long getDownloadTimestamp3() {
        return downloadTimestamp3.orElse(0L);
    }

    public long getDownloadTimestamp4() {
        return downloadTimestamp4.orElse(0L);
    }

}
