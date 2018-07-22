package data;

/**
 * La classe <code>ContinuousAttribute</code> che estende la classe {@link Attribute} e modella un attributo continuo (numerico). 
 * Tale classe include i metodi per la “normalizzazione” del dominio dell'attributo nell'intervallo [0,1] al fine da 
 * rendere confrontabili attributi aventi domini diversi.
 * @author Francesco Ventura
 */
class ContinuousAttribute extends Attribute{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Attributo che rappresenta l'estremo inferiore del dominio.
	 */
	private double min;
	
	/**
	 * Attributo che rappresenta l'estremo superiore del dominio.
	 */
	private double max;
	
	/**
	 * Questo metodo è un costruttore ed invoca, a sua volta, il costruttore della classe madre e 
	 * inizializza i membri aggiunti per estensione
	 * @param name Nome dell'attributo 
	 * @param index Identificativo dell'attributo
	 * @param min Estremo inferiore dell'intervallo
	 * @param max Estremo superiore dell'intervallo
	 */
	ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Calcola e restituisce il valore normalizzato del parametro passato in input. 
	 * La normalizzazione ha come codominio lo intervallo [0,1]. 
	 * La normalizzazione di v è quindi calcolata come segue:
	 * v'=(v-min)/(max-min) 
	 * 
	 * @param v Valore da normalizzare
	 * @return v' Valore normalizzato
	 */
	double getScaledValue(double v) {
		return (v - min) / (max - min);
	}

}
