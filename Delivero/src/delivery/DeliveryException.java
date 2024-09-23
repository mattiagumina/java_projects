package delivery;

@SuppressWarnings("serial")
public class DeliveryException extends Exception {
	public DeliveryException (String reason) {
		super(reason);
	}
}
