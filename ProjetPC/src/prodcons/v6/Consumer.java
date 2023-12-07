package prodcons.v6;

public class Consumer extends Thread {

	public int id;
	public ProdConsBuffer buffer;
	public int consTime;

	public Consumer(int id, ProdConsBuffer buffer, int consTime) {
		this.id = id;
		this.buffer = buffer;
		this.consTime = consTime;
		this.start();
	}

	public void run() {
		try {
			while (true) {
				Message message = buffer.Consume();
				System.out.println("  Consumer " + id + " --->: " + message.mess);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
