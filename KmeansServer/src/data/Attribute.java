package data;

import java.io.Serializable;

/**
 * La classe <code>Attribute</code> è una classe astratta che modella l'entità "attributo"
 * @author Francesco Ventura
 */
abstract class Attribute implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Nome simbolico dell'attributo
	 */
	private String name;
	
	/*
	 * Identificativo numerico dell'attributo
	 */
	private int index;
	
	/**
	 * Costruttore della classe che inizializza i valori dei membri <code>name</code>, <code>index</code>
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo
	 */
	Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Ritorna un oggetto istanza della classe String corrispondente
	 * al nome dell'attributo
	 * @return {@link #name}
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Ritorna un intero corrispondente all'identificativo dell'attributo 
	 * @return {@link #index}
	 */
	int getIndex() {
		return index;
	}
	
	/**
	 * Override del metodo <code>toString()</code> della superclasse Object.
	 * Restuisce la stringa rappresentante lo stato dell'oggetto.
	 * @see Object#toString()
	 * @return {@link #name}
	 */
	@Override
	public String toString() {
		return name;
	}

}
