package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

	private String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver"; // (Per utilizzare questo Driver scaricare e
																	// aggiungere al classpath il connettore mysql
																	// connector)
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost"; // contiene l’identificativo del server su cui risiede la base di 3 dati
												// (per esempio localhost)
	private final String DATABASE = "MapDB";// : contiene il nome della base di dati
	private final int PORT = 3306; // La porta su cui il DBMS MySQL accetta le connessioni
	private final String USER_ID = "MapUser";// contiene il nome dell’utente per l’accesso alla base di dati
	private final String PASSWORD = "map";// contiene la password di autenticazione per l’utente identificato da USER_ID
	private Connection conn; // gestisce una connessione

	public void initConnection() throws DatabaseConnectionException, ClassNotFoundException {
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
		Class.forName(DRIVER_CLASS_NAME);
		try {
			conn = DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Impossibile connettersi al Database!");
		}
	}

	// restituisce conn
	Connection getConnection() {
		return conn;
	}

	// chiude la connessione conn
	void closeConnection() throws SQLException {
		conn.close();
	}

}
