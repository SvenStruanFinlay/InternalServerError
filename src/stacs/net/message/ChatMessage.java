package stacs.net.message;

public class ChatMessage extends Message {
    private static final long serialVersionUID = 2045613638081294354L;

    public final String msg;
    
    public ChatMessage(String msg) {
        this.msg = msg;
    }
    
}
