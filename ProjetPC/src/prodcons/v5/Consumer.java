package prodcons.v5;

public class Consumer extends Thread {

	public long id;
	public ProdConsBuffer buffer;
	public int consTime;

	public Consumer(int id, ProdConsBuffer buffer, int consTime) {
		this.id = this.getId();
		this.buffer = buffer;
		this.consTime = consTime;
		this.start();
	}

	public void run() {
		while (true) {
			try {
				Message[] message = buffer.get(2);
				System.out.println(
						"Consumer n°" + this.getId() + " consomme les message suivants ---->  " + message[0].getMess()+ " et " + message[1].getMess());
				Thread.sleep(consTime); // Simulation du temps de consommation
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
