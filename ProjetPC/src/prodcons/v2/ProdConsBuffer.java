package prodcons.v2;

public class ProdConsBuffer implements IProdConsBuffer {

	int in; // Index d'insertion (producer)
	int out; // Index de retrait (consommateur)
	int totMessage; // Nombre de message rentré dans le buffer depuis le début
	int nbMessage; // Nombre de message dans le buffer
	int nbConsome;

	int bufferSz; // Taille du buffer
	Message buffer[];

	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.in = 0;
		this.out = 0;
		this.totMessage = 0;
		this.nbMessage = 0;
		this.nbConsome = 0;
	}

	/**
	 * Put the message m in the buffer
	 **/
	@Override
	public synchronized void Produce(Message m) throws InterruptedException {

		// Wait until buffer not full
		while (!(nmsg() > 0)) { // Garde
			try {
				wait(); // Le buffer est plein => on attend
			} catch (InterruptedException e) {
			}
		}

		// Un emplacement est disponible pour déposer le message

		buffer[in] = m; // On place le message dans le buffer
		in = (in + 1) % bufferSz; // On change l'indice du buffer

		totMessage++; // On incrémente le nombre de message passé dans le buffer de 1
		nbMessage++; // On incrémente le nombre de message dans le buffer de 1

		notifyAll(); // On réveil les potentiels threads en attente

	}

	/**
	 * Retrieve a message from the buffer, following a FIFO order (if M1 was put
	 * before M2, M1 is retrieved before M2)
	 **/
	@Override
	public synchronized Message Consume() throws InterruptedException {

		// Wait until buffer not empty
		while (!(nbMessage > 0)) { // Garde
			try {
				wait(); // Le buffer est vide => on attend
			} catch (InterruptedException e) {
			}
		}

		Message m = buffer[out]; // On prend le message dans le buffer
		out = (out + 1) % bufferSz; // On ajuste l'index
		
		nbConsome ++;

		nbMessage--; // On décrémente le nombre de message dans le buffer
		notifyAll(); // On réveil les potentiels les threads en attende

		return m; // On renvoie le message

	}

	/**
	 * Returns the number of messages currently available in the buffer
	 **/
	@Override
	public int nmsg() {
		// TODO Auto-generated method stub
		return bufferSz - nbMessage;
	}

	/**
	 * Returns the total number of messages that have been put in the buffer since
	 * its creation
	 **/
	@Override
	public int totmsg() {
		// TODO Auto-generated method stub
		return totMessage;
	}

}
