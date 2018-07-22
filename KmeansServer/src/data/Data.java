package data;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

/**
 * La classe <code>Data</code> modella l'insieme di transazioni (o tuple) ottenute dalla base di dati.
 * Rappresenta tutti i dati che verranno utilizzati all'interno del sistema.
 * Per ottenere correttamente i dati è necessario che all'atto dell'istanziazione della classe
 * sia specificato il nome della tabella, che coincide esattamente con quello della tabella contenuta
 * nella base di dati.
 * @author Francesco Ventura
 */
public class Data implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Lista di esempi memorizzati in una lista.
	 * Ogni esempio modella una transazione.
	 */
	private List<Example> data;
	
	/**
	 * Cardinalità dell’insieme di transazioni
	 */
	private int numberOfExamples;
	
	/**
	 * Lista degli attributi in ciascuna tupla (schema della tabella di dati)
	 */
	private List<Attribute> attributeSet;
	
	/**
	 * 
	 */
	private int distinctTuples;
	
	/**
	 * 
	 * @param table Nome della tabella presente nella base di dati
	 * @throws SQLException 
	 * @throws NoValueException
	 * @throws ClassNotFoundException
	 * @throws DatabaseConnectionException
	 * @throws EmptySetException
	 */
	public Data(String table) throws SQLException, NoValueException, ClassNotFoundException, DatabaseConnectionException, EmptySetException {
		attributeSet = new LinkedList<>();
	
		DbAccess db = new DbAccess();
		db.initConnection();

		TableData tData = new TableData(db);
		TableSchema tSchema = new TableSchema(db, table);

		data = tData.getDistinctTransazioni(table);
		// numberOfExamples
		numberOfExamples = data.size();
		distinctTuples = data.size();

		//explanatory Set
		for(int i = 0; i < tSchema.getNumberOfAttributes(); i++){
			Column column = tSchema.getColumn(i);
			if(!column.isNumber()){					
				Set<Object> vals = tData.getDistinctColumnValues(table, column);	
				TreeSet<String> distinctValues = new TreeSet<>();
				
				for(Object o : vals)
					distinctValues.add(o.toString());
				
				attributeSet.add(new DiscreteAttribute(column.getColumnName(), i, distinctValues));
			}else{
				float min = (float) tData.getAggregateColumnValue(table, column, QUERY_TYPE.MIN);
				float max = (float) tData.getAggregateColumnValue(table, column, QUERY_TYPE.MAX);
				attributeSet.add(new ContinuousAttribute(column.getColumnName(), i, min, max));
			}
		}
	}
	
	/**
	 * Restituisce il numero degli esempi contenenti nel trainingset.
	 * @return {@link #numberOfExamples}
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}
	
	/**
	 * Restituisce il numero degli attributi.
	 * @return Dimensione di <code>attributeSet</code>.
	 */
	public int getNumberOfAttributes(){
		return attributeSet.size();
	}
	
	/**
	 * Restituisce lo schema dei dati-
	 * @return {@link #attributeSet}
	 */
	List<Attribute> getAttributeSchema() {
		return attributeSet;
	}
	
	/**
	 * Valore assunto dall'attributo in posizione attributeIndex dell'esempio 
	 * ricavato dalla lista {@link #data} in posizione exampleIndex.
	 * @param exampleIndex Indice dell'esempio
	 * @param attributeIndex Indice dell'attributo
	 * @return data.get(exampleIndex).get(attributeIndex);
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex){
		return data.get(exampleIndex).get(attributeIndex);
	}
	
	/**
	 * 
	 * @param index
	 * @return 
	 */
	Attribute getAttribute(int index){
		return attributeSet.get(index);
	}
	
	/**
	 * Override del metodo <code>toString()</code> della superclasse Object.
	 * crea una stringa in cui memorizza lo schema della tabella e le 
	 * transazioni memorizzate in {@link #data}, opportunamente enumerate. Restituisce tale stringa.
	 * @return Stringa che modella lo stato dell'oggetto.
	 */
	@Override
	public String toString(){
		String result = new String();
		for(int i = 0; i < getNumberOfAttributes(); i++) {
			result += getAttribute(i).getName();
			if(i < getNumberOfAttributes() - 1)
				result +=", ";	
		}
		result += "\n";
		
		for(int i = 0; i < getNumberOfExamples(); i++) {
			result += i+1 + ":";
			for(int j = 0; j < getNumberOfAttributes(); j++) {
				result += getAttributeValue(i, j);
				if(j < getNumberOfAttributes() - 1)
					result +=", ";	
			}
			result += "\n";
		}
		return result;
	}

	/**
	 * Crea e restituisce un oggetto di Tuple che modella come sequenza di coppie Attributo-valore la i-esima tupla in data.
	 * @param index Indice di riga
	 * @return Restituisce un oggetto di tipo Tuple
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(getNumberOfAttributes());
		
		for(int i = 0; i < getNumberOfAttributes(); i++) {
			Attribute attr = attributeSet.get(i);
			if(attr instanceof DiscreteAttribute)
				tuple.add(new DiscreteItem(attr, (String)data.get(index).get(i)), i);
			else
				tuple.add(new ContinuousItem(attr, (Double)data.get(index).get(i)), i);
		}
		
		return tuple;
	}
	
	/**
	 * @param k Numero di cluster da generare
	 * @return Restituisce un array di k interi rappresentanti gli indici di riga in data per le 5 tuple inizialmente scelte come centroidi (passo 1 del k-means)
	 * @throws OutOfRangeSampleSize
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize{		
		if(k <= 0 || k > this.distinctTuples)	
			throw new OutOfRangeSampleSize("Errore! - Numero inserito non valido!\nRange valori compreso fra 1 e " + this.distinctTuples);
		
		int centroidIndexes[] = new int[k];
		//choose k random different centroids in data.
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());		
		
		for (int i = 0; i < k; i++){
			boolean found = false;
			int c;
			do{
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				// verify that centroid[c] is not equal to a centroide already stored in CentroidIndexes
				for(int j = 0; j < i; j++)
					if(compare(centroidIndexes[j],c)){
						found = true;
						break;
					}
			}while(found);
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}
	
	/**
	 * Restituisce vero se le due tuple di data contengono gli stessi valori, falso altrimenti.
	 * @param i Indice della prima tupla nell’insieme in <code>Data</code>
	 * @param j Indice della seconda tupla da confrontare nell’insieme in <code>Data</code>
	 * @return Restituisce un valore booleano.
	 */
	private boolean compare(int i, int j) {
		int index = 0;
		
		boolean condition = true;
		
		Object first = new Object(),
				sec = new Object();

		while(condition && index < getNumberOfAttributes()){
			
			first = getAttributeValue(i, index);
			sec = getAttributeValue(j, index);
			
			if(!first.equals(sec))
				condition = false;
			
			index++;
		}

		return condition;
	}
	
	/**
	 * Richiama la funzione {@link #computePrototype(Set, ContinuousAttribute)} se l'attributo atteso è di tipo continuo,
	 * altrimenti {@link #computePrototype(Set, DiscreteAttribute)} se l'attributo atteso è di tipo discreto.
	 * @param idList Insieme di indici di riga
	 * @param attribute Attributo rispetto al quale calcolare il prototipo (centroide)
	 * @return Valore centroide rispetto ad <code>attribute</code>
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if(attribute instanceof DiscreteAttribute)
			return computePrototype(idList, (DiscreteAttribute)attribute);
		else
			return computePrototype(idList, (ContinuousAttribute)attribute);
	}
	
	/**
	 * Determina il valore che occorre più frequentemente per <code>attribute</code> 
	 * nel sottoinsieme di dati individuato da <code>idList</code>.
	 * @param idList Insieme degli indici delle righe di data appartenenti as un cluster
	 * @param attribute Attributo discreto rispetto al quale calcolare il prototipo (centroide) 
	 * @return Restituisce il centroide rispetto ad <code>attribute</code>.
	 */
	String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		int maxFr = 0; 
		int attrFr = 0; 
		
		String prototype = new String();
		
		Iterator<String> stringIterator = attribute.iterator();
		
		while(stringIterator.hasNext()) {
			String s = stringIterator.next();
			attrFr = attribute.frequency(this, idList, s);

			if(maxFr < attrFr){
				maxFr = attrFr;
				prototype = s;
			}
		}
		
		return prototype;
	}
	
	/**
	 * 
	 * @param idList
	 * @param attribute
	 * @return
	 */
	Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double adds = 0, avg;

		for(Integer in : idList)
			adds = adds + (Double)(data.get(in).get(1));

		avg = adds / idList.size();

		return avg;
	}
	
	/**
	 * Metodo main che permette la stampa dell'insieme di transazioni
	 * memorizzate in {@link Data}.
	 * @param args Argomenti in input
	 */
	public static void main(String args[]){
		Data trainingSet = null;
		try {
			trainingSet = new Data("playtennis");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoValueException e) {
			e.printStackTrace();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		} catch (EmptySetException e) {
			e.printStackTrace();
		}
		System.out.println(trainingSet);	
	}

}