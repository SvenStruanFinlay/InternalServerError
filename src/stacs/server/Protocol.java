package stacs.server;

public class Protocol {
	
	public enum P {
		StartTurn("StartTurn"),
		Chat("Chat");

		String msg;
		
		P(String msg) {
			this.msg = msg;
		}
		
		public String toString() {
			return this.msg;
		}
		
	}
	
}
