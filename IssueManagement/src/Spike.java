
public class Spike {

	public static void main(String[] args) {
		String s = "/System/Sub";
		
		System.out.println("'" + s.replaceAll("/[^/]+$","")+"'");

	}

}
