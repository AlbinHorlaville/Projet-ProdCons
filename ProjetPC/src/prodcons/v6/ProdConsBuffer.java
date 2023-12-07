package prodcons.v6;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; // Indice d'insertion (producer)
	int out; // Indice de retrait (consommateur)
	int totMessage; // Nombre demessage rentré dans le buffer depuis le début
	int nbMessage; // Nombre de message dans le buffer

	int bufferSz; // Taille du buffer
	Message buffer[];
	Semaphore mutex;

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		this.nbMessage = 0;
		this.mutex = new Semaphore(1,true);
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
		// tant qu'il reste le même message à consommer le consommateur attend qu'un autre le consomme
		while (buffer[out].equals(m)){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		notifyAll();
		return m;
	}

	@Override
	public  synchronized Message[] get(int k) throws InterruptedException {

		Message[] messages = new Message[0];
		// Wait until buffer not empty
		while (!(nbMessage > 0)) { // Garde
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		Message[] newM = new Message[k];
		for (int i = 0; i < k; i++) {
			for (int j = 0; i < messages.length; i++) {
				newM[j] = messages[j];
			}
			messages = newM;
			messages[messages.length - 1] = buffer[out];
			out = (out + 1) % bufferSz;

			// One more not empty entry
			nbMessage--;
		}
		notifyAll();

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

	@Override
	public synchronized void put(Message m, int n) throws InterruptedException {

		// Wait until buffer not full
		while (!(nmsg() > 0)) { // Garde
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		for (int i = 0; i < n; i++) {
			buffer[in] = m; // On ajoute le msg au buffer
			in = (in + 1) % bufferSz; // On change l'indice du buffer
			totMessage++;
			nbMessage++;
		}
		notifyAll();
	}

}
