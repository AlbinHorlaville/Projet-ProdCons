package prodcons.v5;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; // Indice d'insertion (producer)
	int out; // Indice de retrait (consommateur)
	int totMessage; // Nombre demessage rentré dans le buffer depuis le début
	int nbMessage; // Nombre de message dans le buffer

	int nbConsomme;

	int bufferSz; // Taille du buffer
	Message buffer[];
	Semaphore mutex;

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		this.nbConsomme = 0;
		this.nbMessage = 0;
		this.mutex = new Semaphore(1, true);
	}

	/* put */
	@Override
	public synchronized void Produce(Message m) throws InterruptedException {
		// TODO Auto-generated method stub

		// Wait until buffer not full
		while (!(nmsg() > 0)) { // Garde
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		buffer[in] = m; // On ajoute le msg au buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		totMessage++;
		nbMessage++;

		notifyAll();

	}

	/* get */
	@Override
	public synchronized Message Consume() throws InterruptedException {

		// Wait until buffer not empty
		while (!(nbMessage > 0)) { // Garde
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		Message m = buffer[out];
		out = (out + 1) % bufferSz;

		// One more not empty entry
		nbMessage--;
		notifyAll();
		return m;

	}

	@Override
	public Message[] get(int k) throws InterruptedException {
		mutex.acquire();
		Message[] messages = new Message[k];

		synchronized (this) {
			for (int i = 0; i < k; i++) {
				try {
					// On attend tant qu'il n'y a rien dans le buffer aka tant qu'il n'y a rien à
					// lire
					while (nbMessage < 0) {
						wait();
					}

					messages[i] = buffer[out];
					out = (out + 1) % bufferSz;
					nbMessage--;
					mutex.release();
					notifyAll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return messages;

	}

	@Override
	public int nmsg() {
		// TODO Auto-generated method stub
		return bufferSz - nbMessage;
	}

	@Override
	public int totmsg() {
		// TODO Auto-generated method stub
		return totMessage;
	}

}
