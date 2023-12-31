package prodcons.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {
	
	
	public static void main(String[] argv) throws InvalidPropertiesFormatException, IOException, InterruptedException {
		
		
		int nProd, nCons, bufSz, prodTime, consTime, minProd, maxProd;

		Properties properties = new Properties();
		properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("prodcons/v1/Options.xml"));

		nProd = Integer.parseInt(properties.getProperty("nProd"));
		nCons = Integer.parseInt(properties.getProperty("nCons"));
		bufSz = Integer.parseInt(properties.getProperty("bufSz"));
		prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		consTime = Integer.parseInt(properties.getProperty("consTime"));
		minProd = Integer.parseInt(properties.getProperty("minProd"));
		maxProd = Integer.parseInt(properties.getProperty("maxProd"));

		/* Test des producteurs consommateurs */

		ProdConsBuffer buffer = new ProdConsBuffer(bufSz);

		// Création des threads producteurs et consommateurs

		ArrayList<Producer> tabProd = new ArrayList<Producer>();
		ArrayList<Consumer> tabCons = new ArrayList<Consumer>();

		for (int i = 0; i < nProd; i++) {
			tabProd.add(new Producer(i + 1, buffer, prodTime, minProd, maxProd));
		}

		for (int i = 0; i < nCons; i++) {
			tabCons.add(new Consumer(i + 1, buffer, consTime));
		}
		
		for(Producer p : tabProd) {
			try {
				p.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		buffer.finished = true;
		
		System.out.println("Total Messages Produits: " + buffer.totmsg());

	}

}
