package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

public class TableData {

	private DbAccess db;

	public TableData(DbAccess db) {
		this.db = db;
	}

	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
		List<Example> examples = new LinkedList<Example>();

		Statement st;
		String query = "select distinct ";
		TableSchema tSchema = new TableSchema(db, table);

		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			if (i > 0)
				query += ", ";
			query += tSchema.getColumn(i).getColumnName();
		}

		if (tSchema.getNumberOfAttributes() == 0)
			throw new SQLException();

		query += " FROM " + table;

		st = db.getConnection().createStatement();
		ResultSet rs = st.executeQuery(query);
		
		if (!rs.isBeforeFirst())
			throw new EmptySetException("Nessun risultato trovato!");
		else
			while (rs.next()) {
				Example ex = new Example();
				for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
					String col = tSchema.getColumn(i).getColumnName();
					if (tSchema.getColumn(i).isNumber())
						ex.add(rs.getDouble(col));
					else
						ex.add(rs.getString(col));
				}
				examples.add(ex);
			}
		return examples;
	}

	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Set<Object> values = new TreeSet<Object>();

		Statement st;
		String colname = column.getColumnName();
		String query = "SELECT " + colname + " FROM " + table + " ORDER BY " + colname + " ASC ;";
		
		st = db.getConnection().createStatement();
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) 
			values.add(rs.getObject(column.getColumnName()));

		rs.close();
		return values;
	}

	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
			throws SQLException, NoValueException {
		
		Object value = null;
		String aggOperator = "";
		String query = "select ";
		String colname = column.getColumnName();

		if(aggregate == QUERY_TYPE.MAX)
			aggOperator += "max";
		else
			aggOperator += "min";

		query += aggOperator + "(" + colname + ") FROM " + table;

		Statement st = db.getConnection().createStatement();
		ResultSet rs = st.executeQuery(query);
		
		if(rs.next()) 
			value = rs.getObject(1);
		
		rs.close();
		st.close();		
		
		if(value == null)
			throw new NoValueException("Impossibile effettuare " + aggOperator + " su " + colname);
		
		return value;
	}

}
