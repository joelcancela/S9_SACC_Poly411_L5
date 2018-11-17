package fr.unice.polytech.si5.cc.l5.model;

import java.util.*;

/**
 * Class User
 *
 * @author Joël CANCELA VAZ
 */
public class User {
    private String email;
    private String name;
    private double score;
    private OptionalLong downloadTimestamp1 = OptionalLong.empty();
    private OptionalLong downloadTimestamp2 = OptionalLong.empty();
    private OptionalLong downloadTimestamp3 = OptionalLong.empty();
    private OptionalLong downloadTimestamp4 = OptionalLong.empty();

    public User(String email, String name) {
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
