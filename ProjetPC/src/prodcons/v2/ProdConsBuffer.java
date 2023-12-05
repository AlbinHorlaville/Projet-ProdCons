package prodcons.v2;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; 		  // Indice d'insertion (producer)
	int out;		  // Indice de retrait (consommateur)
	int totMessage;   // Nombre demessage rentré dans le buffer depuis le début
	int nbMessage;		  // Nombre de message dans le buffer
	
	int nbConsome;

	int bufferSz;	  // Taille du buffer
	Message buffer[]; 

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		nbConsome = 0;
		this.nbMessage = 0;
	}

	/* put */
	@Override
	public synchronized void Produce(Message m) throws InterruptedException {
		// TODO Auto-generated method stub

		// Wait until buffer not full
		while (!(nmsg() > 0)) {		// Garde
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		buffer[in] = m; // On ajoute le msg au buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		totMessage ++;
		nbMessage ++;

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
		nbConsome ++;
		nbMessage --;
		notifyAll(); 
		return m;

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
