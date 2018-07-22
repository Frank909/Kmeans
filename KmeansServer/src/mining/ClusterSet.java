package mining;
import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

public class ClusterSet implements Serializable{

	private static final long serialVersionUID = 1L;

	private Cluster C[];
	
	private int i = 0;
	
	ClusterSet(int k){
		C = new Cluster[k];
	}
	
	void add(Cluster c) {
		C[i] = c;
		i++;
	}
	
	Cluster get(int i) {
		return C[i];
	}
	
	void initializeCentroids(Data data) throws OutOfRangeSampleSize{
		int centroidIndexes[];
		
		centroidIndexes = data.sampling(C.length);
		for(int i = 0; i < centroidIndexes.length; i++){
			Tuple centroidI = data.getItemSet(centroidIndexes[i]);
			add(new Cluster(centroidI));
		}		
	}
	
	Cluster nearestCluster(Tuple tuple) {		
		double minDist = tuple.getDistance(C[0].getCentroid());
		int index = 0;
		
		for(int i = 1; i < C.length; i++){
			double currentDist = tuple.getDistance(C[i].getCentroid());
			if(minDist > currentDist){
				minDist = currentDist;
				index = i;
			}
		}
		
		return get(index);
	}
	
	Cluster currentCluster(int id) {
		int index = 0;
		Cluster cluster = null;
		
		for(int i = 0; i < C.length; i++){
			if(this.get(i).contain(id)){
				index = i;
				cluster = get(index);
			}
		}
		
		return cluster;	
	}
	
	void updateCentroids(Data data) {
		for(int i = 0; i < C.length; i++)
			C[i].computeCentroid(data);
	}
	
	@Override
	public String toString(){
		String result = new String();
		
		for(int i = 0; i < C.length; i++)
			result += C[i].toString() + "\n";
				
		return result;
	}
	
	public String toString(Data data){
		String str = "";		
		for(int i = 0; i < C.length; i++){
			if (C[i] != null)
				str += i + ":" + C[i].toString(data) + "\n";			
		}
		
		return str;
	}

}
