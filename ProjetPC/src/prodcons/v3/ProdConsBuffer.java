package prodcons.v3;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; // Indice d'insertion (producer)
	int out; // Indice de retrait (consommateur)
	int totMessage; // Nombre demessage rentré dans le buffer depuis le début
	int nbMessage; // Nombre de message dans le buffer

	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore mutexIn;
	Semaphore mutexOut;

	int bufferSz; // Taille du buffer
	Message buffer[];

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		this.nbMessage = 0;
		this.notEmpty = new Semaphore(0, true);
		this.notFull = new Semaphore(bufferSz, true);
		this.mutexIn = new Semaphore(1, true); // Verrou du producer : un seul thread pourra poser une un msg
		this.mutexOut = new Semaphore(1, true); // Verrou du consumer : un seul thread pourra prendre un msg
	}

	/* put */
	@Override
	public void Produce(Message m) throws InterruptedException {
		// TODO Auto-generated method stub

		// Wait until buffer not full
		notFull.acquire(); // On prend un ressource
		mutexIn.acquire(); // On met un verrou

		buffer[in] = m; // On ajoute le msg au buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		totMessage++;
		nbMessage++;

		mutexIn.release(); // On libère le verroou
		notEmpty.release(); // On libère une ressource

	}

	/* get */
	@Override
	public Message Consume() throws InterruptedException {

		// Wait until buffer not empty
		notEmpty.acquire();
		mutexOut.acquire();

		Message m = buffer[out];
		out = (out + 1) % bufferSz;

		// One more not empty entry
		nbMessage--;

		mutexOut.release();
		notFull.release();

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
