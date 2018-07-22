package data;

/**
 * La classe <code>DiscreteItem</code> estende la classe {@link Item} 
 * e rappresenta una coppia <Attributo discreto- valore discreto> (per esempio Outlook = "Sunny").
 * @author Francesco Ventura
 */
class DiscreteItem extends Item{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore della classe <code>DiscreteItem</code>
	 * Invoca il costruttore della classe madre inizializzando l'attributo coninvolto
	 * nell'item di tipo {@link DiscreteAttribute} assegnandoli il valore specificato.
	 * @param attribute DiscreteAttribute coinvolto nell'item corrente.
	 * @param value	Valore da assegnare all'item.
	 */
	DiscreteItem(Attribute attribute, Object value) {
		super(attribute, value);
	}

	/**
	 * Definisce il metodo astratto ereditato dalla classe madre {@link Item}.
	 * Restituirà 0 se il valore dell'attributo corrente è uguale al valore 
	 * specificato come parametro ({@link Object#toString()} dell'oggetto specificato). 
	 * Se le due stringhe sono diverse viene restituito 1.
	 */
	@Override
	double distance(Object a) {		
		if(getValue().equals(a.toString()))
			return 0;
		else
			return 1;
	}

}
