package prodcons.v5;

import java.util.Random;

public class Producer extends Thread {
	long id;
	int minProd;
	int maxProd;
	int prodTime;
	Random alea; // Nombre aléatoire entre minProd et maxProd
	ProdConsBuffer buffer;

	public Producer(int id, ProdConsBuffer buffer2, int prodTime, int minProd, int maxProd) {
		this.id = this.getId();
		this.buffer = buffer2;
		this.minProd = minProd;
		this.maxProd = maxProd;
		this.prodTime = prodTime;
		this.alea = new Random();
		this.start();
	}

	public void run() {
		try {
			int nbMessageAproduire = alea.nextInt(maxProd - minProd + 1) + minProd; // Nombre de message que notre
																					// thread va produire

			for (int i = 0; i < nbMessageAproduire; i++) {
				Message message = new Message(
						"Producer n° " + this.getId() + " crée le message suivant : Message n° " + (i + 1));
				buffer.Produce(message); // Dépot du message dans le buffer
				Thread.sleep(prodTime); // Simulation du temps de production
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
