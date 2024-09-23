package sports;

public class Rating {
	
	String productName;
	String userName;
	int numStars;
	String comment;
	
	public Rating(String productName, String userName, int numStars, String comment) {
		this.productName = productName;
		this.userName = userName;
		this.numStars = numStars;
		this.comment = comment;
	}
	
	public String toString() {
		return this.numStars + " : " + this.comment;
	}

}
