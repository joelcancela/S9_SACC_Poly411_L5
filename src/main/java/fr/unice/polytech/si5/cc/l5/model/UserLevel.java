package fr.unice.polytech.si5.cc.l5.model;

/**
 * Enum UserLevel
 *
 * @author Joël CANCELA VAZ
 */
public enum UserLevel {
	NOOB(1, 5, "noob"), CASUAL(2, 10, "casual"), LEET(4, 30, "leet");
	private int requestsPerMinute;
	private int fileConservationTimeout;
	private String desc;

	UserLevel(int requestsPerMinute, int fileConservationTimeout, String desc) {
		this.requestsPerMinute = requestsPerMinute;
		this.fileConservationTimeout = fileConservationTimeout;
		this.desc = desc;
	}

	public static UserLevel pointsToRank(double points) {
		if (points < 100) {
			return NOOB;
		} else if (points < 200) {
			return CASUAL;
		} else {
			return LEET;
		}
	}

	public String desc() {
	    return desc;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public int getFileConservationTimeout() { // in ms
        return fileConservationTimeout * 60 * 1000;
    }
}
