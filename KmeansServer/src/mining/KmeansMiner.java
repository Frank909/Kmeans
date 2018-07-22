package mining;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.Data;
import data.OutOfRangeSampleSize;

public class KmeansMiner{

	private ClusterSet C;
	
	public KmeansMiner(int k) {
		C = new ClusterSet(k);
	}
	
	public KmeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException{
		FileInputStream file = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(file);
		C = (ClusterSet)in.readObject();
		in.close();
		file.close();
	}
	
	public void salva(String fileName) throws FileNotFoundException, IOException{
		FileOutputStream file = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.writeObject(C);
		out.flush();
		file.close();
	}
	
	public ClusterSet getC() {
		return C;
	}

	public int kmeans(Data data) throws OutOfRangeSampleSize{
		int numberOfIterations = 0;
		//STEP 1
		C.initializeCentroids(data);
		boolean changedCluster = false;
		do{
			numberOfIterations++;
			//STEP 2
			changedCluster = false;
			for(int i = 0; i < data.getNumberOfExamples(); i++){
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster = C.currentCluster(i);
				boolean currentChange = nearestCluster.addData(i);
				if(currentChange)
					changedCluster = true;
					//rimuovo la tupla dal vecchio cluster
				if(currentChange && oldCluster != null)
					//il nodo va rimosso dal suo vecchio cluster
					oldCluster.removeTuple(i);
			}
			//STEP 3
			C.updateCentroids(data);
		}while(changedCluster);
		return numberOfIterations;
	}
}
