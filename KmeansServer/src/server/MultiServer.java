package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class MultiServer {

	private int PORT = 8080;
	
	public static void main(String[] args) {
		new MultiServer(8080);
	}
	
	MultiServer(int PORT) {
		this.PORT = PORT;
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run() throws IOException {
		ServerSocket s = new ServerSocket(PORT);;
		try {
			System.out.println("Server Avviato");
			while(true) {
				Socket socket = s.accept();
				System.out.println("Connessione socket: " + socket);
				try{
					new ServerOneClient(socket);
				}catch (IOException e){
					System.out.println("\nConnessione fallita.");
					socket.close();
				}
			}					
		} finally {
			s.close();
		}
	}
}
