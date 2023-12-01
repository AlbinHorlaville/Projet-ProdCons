package prodcons.v3;

public class Message {
	
	public int producerId;
    public String mess;

    public Message(int producerId, String msg) {
        this.producerId = producerId;
        this.mess = msg;
    }

}
