package fr.unice.polytech.si5.cc.l5.model;

/**
 * Class User
 *
 * @author Joël CANCELA VAZ
 */
public class User {
	private String email;
	private String name;
	private double score;

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


}
