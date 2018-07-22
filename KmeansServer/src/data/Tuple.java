package data;

import java.io.Serializable;
import java.util.Set;

/**
 * La classe <code>Tuple</code> rappresenta una tupla come sequenza di coppie attributo-valore. 
 * @author Francesco Ventura
 */
public class Tuple implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Array di <code>Item</code>. 
	 */
	private Item[] tuple;
	
	/**
	 * Costruttore della classe <code>Tuple</code>.
	 * Costruisce l'oggetto riferito da {@link #tuple}.
	 * @param size Numero di item che costituirà la tupla.
	 */
	Tuple(int size) {
		tuple = new Item[size];
	}
	
	/**
	 * @return Restituisce lunghezze dell'array {@link #tuple}.
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * @param i Posizione dell'array
	 * @return Restituisce l'Item in posizione i nell'array {@link #tuple}.
	 */
	public Item get(int i) {
		return tuple[i];
	}
	
	/**
	 * Metodo che memorizza l'Item c nella posizione i-esima nell'array {@link #tuple}.
	 * @param c Item da memorizzare
	 * @param i Indice dell'array
	 */
	void add(Item c, int i) {
		tuple[i] = c;
	}
	
	/**
	 * Determina la distanza tra la tupla riferita da obj e la tupla corrente (riferita da this). 
	 * La distanza è ottenuta come la somma delle distanze tra gli item in posizioni eguali nelle due tuple.
	 * @param obj Tupla da confrontare con quella corrente.
	 * @return Valore double uguale alla distanza tra le due tuple.
	 */
	public double getDistance(Tuple obj) {
		double dist = 0;
		
		for(int i = 0; i < obj.getLength(); i++)
			dist = dist + tuple[i].distance(obj.get(i)); 
		
		return dist;
	}
	
	/**
	 * Restituisce la media delle distanze tra la tupla corrente e quelle ottenibili dalle righe 
	 * della tabella in data aventi indice in clusteredData.
	 * @param data Oggetto istanza della classe Data.
	 * @param clusteredData Insieme degli indici delle tuple considerate.
	 * @return Valore double contenente il calcolo della media delle distanze.
	 */
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		double p = 0.0, sumD = 0.0;
		for(Integer in : clusteredData){
			double d = getDistance(data.getItemSet(in));
			sumD += d;
		}		
		p = sumD / clusteredData.size();		
		return p;
	}

}
