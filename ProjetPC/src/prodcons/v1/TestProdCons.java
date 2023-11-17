package prodcons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] argv) throws InvalidPropertiesFormatException, IOException {
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
		
	}

}
