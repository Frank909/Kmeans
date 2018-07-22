package data;

import java.io.Serializable;
import java.util.Set;

/**
 * La classe astratta <code>Item</code> che modella un generico item 
 * (coppia attributo-valore, per esempio Outlook = "Sunny").
 * @author Francesco Ventura
 */
public abstract class Item implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Attributo coinvolto nell'item
	 */
	private Attribute attribute;
	
	/**
	 * Valore assegnato all'attributo
	 */
	private Object value;
	
	/**
	 * Costruttore della classe <code>Item</code>.
	 * Inizializza i valori dei membri attributi.
	 * @param attribute Attributo coinvolto nell'item
	 * @param value Valore assegnato all'attributo
	 */
	Item(Attribute attribute, Object value){
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * @return Restituisce {@link #attribute}
	 */
	Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * @return Restituisce {@link #value}
	 */
	Object getValue() {
		return value;
	}
	
	/**
	 * Override del metodo <code>toString()</code> della superclasse Object.
	 * @return Restituisce {@link #value} convertendolo in stringa.
	 */
	@Override
	public String toString() {
		return value.toString();
	}
	
	/**
	 * L’implementazione sarà diversa per item discreto e item continuo.
	 * @param Oggetto che potrà essere un attributo di tipo discreto/continuo. 
	 * @return Valore di tipo double contenente il risultato del metodo.
	 */
	abstract double distance(Object a);
	
	/**
	 * Il metodo modifica il membro value, assegnandogli il valore
	 * restituito da {@link Data#computePrototype(Set, Attribute)}.
	 * @param data Riferimento ad un oggetto della classe Data.
	 * @param clusteredData Insieme di indici degli esempi nella lista in data che formano i cluster.
	 */
	public void update(Data data, Set<Integer> clusteredData){
		value = data.computePrototype(clusteredData, attribute);
	}

}
