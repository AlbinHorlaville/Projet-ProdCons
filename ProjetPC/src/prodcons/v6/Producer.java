package prodcons.v6;

import java.util.Random;

public class Producer extends Thread {
	int id;
	int minProd;
	int maxProd;
	int prodTime;
	Random alea; // Nombre aléatoire entre minProd et maxProd
	ProdConsBuffer buffer;
	int n;

	public Producer(int id, ProdConsBuffer buffer, int prodTime, int minProd, int maxProd) {
		this.id = id;
		this.buffer = buffer;
		this.minProd = minProd;
		this.maxProd = maxProd;
		this.prodTime = prodTime;
		this.alea = new Random();
		this.n = 5;
		this.start();
	}

	public void run() {
		try {
			int messageAproduire = alea.nextInt(maxProd - minProd + 1) + minProd;
			for (int i = 0; i < messageAproduire; i++) {
				Message message = new Message(id , "Message " + id);
				buffer.put(message, n);
				System.out.println("Producer " + id + " --->: " + message.mess);
				Thread.sleep(prodTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
