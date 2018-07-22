package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import data.Data;
import data.OutOfRangeSampleSize;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.KmeansMiner;

class ServerOneClient extends Thread{
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private KmeansMiner kmeans;
	
	ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
		this.start();
	}
	
	@Override
	public void run() {
		String tableName = null;
		System.out.println("Nuovo client connesso");
		Data data = null;
		int k = 0;
		try {
			while(true) {
				int command = 0;
				command = ((Integer)(in.readObject())).intValue();
				switch(command) {
					case 0:
						tableName = (String)in.readObject();
						try {
							data = new Data(tableName);
							out.writeObject("OK");
						} catch (SQLException e) {
							out.writeObject(e.getMessage());
						} catch (NoValueException e) {
							out.writeObject(e.getMessage());
						} catch (DatabaseConnectionException e) {
							out.writeObject(e.getMessage());
						} catch (EmptySetException e) {
							out.writeObject(e.getMessage());
						}												
						break;
					case 1:
						k = (int) in.readObject();
						kmeans = new KmeansMiner(k);
						try {
							kmeans.kmeans(data);
							out.writeObject("OK");
							out.writeObject(k);
							out.writeObject(kmeans.getC().toString(data));
						} catch (OutOfRangeSampleSize e) {
							out.writeObject(e.getMessage());
						}
						break;
					case 2:
						try {
							kmeans.salva(tableName + k);
							out.writeObject("OK");
						}catch(IOException e) {
							out.writeObject("Impossibile effettuare il salvataggio su file.");
						}
						break;
					case 3:
						String fileName = (String) in.readObject();
						int it = (int) in.readObject();
						try{
							kmeans = new KmeansMiner(fileName + it);
							out.writeObject("OK");
							out.writeObject(kmeans.getC().toString());
						}catch(FileNotFoundException e){
							out.writeObject(fileName + it + ": File non trovato.");
						}						
						break;
				}
			}
		} catch (SocketException e) {
			return;
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
				System.out.println("Client disconnesso.");
			} catch (IOException e) {
				System.out.println("Socket non chiuso correttamente");
			}
		}
	}

}
