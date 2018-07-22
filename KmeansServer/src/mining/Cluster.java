package mining;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import data.Data;
import data.Tuple;

public class Cluster implements Serializable{

	private static final long serialVersionUID = 1L;
	private Tuple centroid;

	private Set<Integer> clusteredData; 

	Cluster(Tuple centroid){
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();		
	}
		
	Tuple getCentroid(){
		return centroid;
	}
	
	void computeCentroid(Data data){
		for(int i = 0; i < centroid.getLength(); i++){
			centroid.get(i).update(data, clusteredData);			
		}
		
	}
	//return true if the tuple is changing cluster
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	
	//verifica se una transazione è clusterizzata nell'array corrente
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	

	//remove the tuplethat has changed the cluster
	void removeTuple(int id){
		clusteredData.remove(id);		
	}
	
	@Override
	public String toString(){
		String str = "Centroid=(";
		for(int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + " ";
		str += ")";
		return str;
		
	}
	
	public String toString(Data data){
		String str = "Centroid=(";
		
		for(int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i)+ " ";
		
		str += ")\nExamples:\n";		
		for(Integer in : clusteredData) {
			str += "[";
			for(int j = 0; j < data.getNumberOfAttributes(); j++)
				str += data.getAttributeValue(in, j) + " ";
			str += "] dist=" + getCentroid().getDistance(data.getItemSet(in)) + "\n";
		}
			
		str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData);
		
		return str;
	}

}
