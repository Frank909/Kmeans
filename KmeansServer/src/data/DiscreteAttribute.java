package data;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * La classe estende la classe {@link Attribute} e rappresenta un attributo discreto (categorico)
 * L'attributo è caratterizzato da un insieme di valori che lo rappresentano, oltre che da
 * un indice e da un nome, ereditati dalla classe madre.
 * Implementa l'interfaccia <code>Iterable</code>.
 * @author Francesco Ventura
 */
class DiscreteAttribute extends Attribute implements Iterable<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * TreeSet di oggetti String, uno per ciascun valore del dominio discreto.
	 * I valori del dominio sono univoci e sono ordinati seguendo un ordine lessicografico.
	 * @see java.util.TreeSet
	 */
	private TreeSet<String> values;

	/**
	 * Costruttore della classe <code>DiscreteAttribute</code>
	 * Invoca il costruttore della classe madre e 
	 * inizializza il membro <code>values</code> con il parametro in input.
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo
	 * @param values TreeSet di stringhe rappresentanti il dominio dell'attributo
	 */
	DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Restituisce il numero di elementi il dominio dell'attributo 
	 * presenti nel TreeSet {@link #values}.
	 * @return {@link java.util.TreeSet#size()}
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}

	/**
	 * Determina il numero di volte che il valore <code>v</code> compare in corrispondenza dell'attributo corrente 
	 * negli esempi memorizzati in <code>data</code> e indicizzate da <code>idList</code>
	 * @param data Riferimento ad un oggetto Data
	 * @param idList riferimento ad un oggetto Set di stringhe
	 * @param v Valore discreto
	 * @return Numero di occorrenze del valore discreto (intero)
	 */
	int frequency(Data data, Set<Integer> idList, String v) {
		int fr = 0;
		int index = this.getIndex();

		for(Object o : idList)
			if(v.equals(data.getAttributeValue((Integer)o, index)))
				fr++;

		return fr;	
	}

	/**
	 * Override del metodo {@link Iterable#iterator()} dell'interfaccia <code>Iterable</code>.
	 * Ritorna l'iteratore del TreeSet {@link #values}
	 * @return {@link java.util.TreeSet#iterator()}
	 */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}
