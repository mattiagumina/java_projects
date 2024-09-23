package transactions;

public class Transaction {

	String id;
	String carrier;
	String requestId;
	String offerId;
	int score;
	
	public Transaction(String id, String carrier, String requestId, String offerId) {
		this.id = id;
		this.carrier = carrier;
		this.requestId = requestId;
		this.offerId = offerId;
		this.score = 0;
	}
}
