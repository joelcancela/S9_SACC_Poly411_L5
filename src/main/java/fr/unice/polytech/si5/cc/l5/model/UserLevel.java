package fr.unice.polytech.si5.cc.l5.model;

/**
 * Enum UserLevel
 *
 * @author Joël CANCELA VAZ
 */
public enum UserLevel {
	NOOB(1, 5), CASUAL(2, 10), LEET(4, 10);
	private int requestsPerMinute;
	private int fileConservationTimeout;

	UserLevel(int requestsPerMinute, int fileConservationTimeout) {
		this.requestsPerMinute = requestsPerMinute;
		this.fileConservationTimeout = fileConservationTimeout;
	}

	public static String pointsToRank(double points) {
		if (points < 100) {
			return "Noob";
		} else if (points < 200) {
			return "Casual";
		} else {
			return "Leet";
		}
	}
}
