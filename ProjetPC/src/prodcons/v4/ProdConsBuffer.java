package prodcons.v4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; // Indice d'insertion (producer)
	int out; // Indice de retrait (consommateur)
	int totMessage; // Nombre demessage rentré dans le buffer depuis le début
	int nbMessage; // Nombre de message dans le buffer

	int bufferSz; // Taille du buffer
	Message buffer[];

	private final Lock lock;
	private final Condition isEmpty;
	private final Condition isFull;

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		this.nbMessage = 0;

		lock = new ReentrantLock();
		isEmpty = lock.newCondition();
		isFull = lock.newCondition();
	}

	/* put */
	@Override
	public void Produce(Message m) throws InterruptedException {
		lock.lock();

		while (nbMessage >= bufferSz) {
			isFull.await();
		}

		buffer[in] = m; // On ajoute le msg au buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		totMessage++;
		nbMessage++;

		isEmpty.signal();
		lock.unlock();
	}

	/* get */
	@Override
	public Message Consume() throws InterruptedException {

		// Wait until buffer not empty

		Message m = null;

		lock.lock();

		while (nbMessage <= 0) {
			isEmpty.await();
		}

		m = buffer[out];
		out = (out + 1) % bufferSz;

		// One more not empty entry
		nbMessage--;

		isFull.signal();
		lock.unlock();

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
