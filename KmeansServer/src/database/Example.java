package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Francesco Ventura
 *
 */
public class Example implements Comparable<Example>, Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<Object> example = new ArrayList<Object>();

	public void add(Object o){
		example.add(o);
	}
	
	public Object get(int i){
		return example.get(i);
	}
	
	public int compareTo(Example ex) {		
		int i = 0;
		for(Object o : ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	public String toString(){
		String str = "";
		for(Object o : example)
			str += o.toString() + " ";
		return str;
	}
	
}
