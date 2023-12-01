package prodcons.v5;

import java.util.Random;

public class Producer extends Thread {
	int id;
	int minProd;
	int maxProd;
	int prodTime;
	Random alea; // Nombre aléatoire entre minProd et maxProd
	ProdConsBuffer buffer;

	public Producer(int id, ProdConsBuffer buffer, int prodTime, int minProd, int maxProd) {
		this.id = id;
		this.buffer = buffer;
		this.minProd = minProd;
		this.maxProd = maxProd;
		this.prodTime = prodTime;
		this.alea = new Random();
		this.start();
	}

	public void run() {
		try {
			int messageAproduire = alea.nextInt(maxProd - minProd + 1) + minProd;
			for (int i = 0; i < messageAproduire; i++) {
				Message message = new Message(id , "Message " + id);
				buffer.Produce(message);
				System.out.println("Producer " + id + " --->: " + message.mess);
				Thread.sleep(prodTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
