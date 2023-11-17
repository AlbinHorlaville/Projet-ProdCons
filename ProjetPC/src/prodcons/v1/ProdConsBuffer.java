package prodcons.v1;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
	int nfull;
	int nempty;
	int in;
	int out;
	int nb_msg;

	int bufferSz;
	Message buffer[]; // Donnée partagé

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.nfull = bufferSz;
		this.nempty = 0;
		this.in = 0;
		this.out = 0;
		this.nb_msg = 0;

	}

	/* put */
	@Override
	public synchronized void Produce(Message m) throws InterruptedException {
		// TODO Auto-generated method stub

		// Wait until buffer not full
		while (nfull == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		buffer[in] = m; // On ajoute le msg au buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		nb_msg++;

		// One more not empty entry
		nfull = nfull - 1;

		notifyAll(); // Pas sur

	}

	/* get */
	@Override
	public synchronized Message Consume() throws InterruptedException {

		// Wait until buffer not empty
		while (nempty >= bufferSz) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		Message m = buffer[out];
		out = (out + 1) % bufferSz;

		// One more not empty entry
		nempty = nempty + 1;

		notifyAll(); // Pas sur

	}

	@Override
	public int nmsg() {
		// TODO Auto-generated method stub
		return in;
	}

	@Override
	public int totmsg() {
		// TODO Auto-generated method stub
		return nb_msg;
	}

}
