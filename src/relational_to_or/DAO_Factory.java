package relational_to_or;

import java.sql.*;

public class DAO_Factory {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/information_schema";
	String dbname;
	String USER;
	String PASS;
	Connection dbconnection = null;
	
	ReverseEnggDAO rdao = null;
	
	boolean activeConnection = false;

	public DAO_Factory(String name, String user, String password)
	{
		dbname = name;
		USER = user;
		PASS = password;
		dbconnection = null;
		activeConnection = false;
	}

	public void activateConnection() throws Exception
	{
		if( activeConnection == true )
			throw new Exception("Connection already active");

		try{
			Class.forName("com.mysql.jdbc.Driver");
			dbconnection = DriverManager.getConnection(DB_URL,USER,PASS);
			activeConnection = true;
		} catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public ReverseEnggDAO getReverseEnggDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( rdao == null )
			rdao = new ReverseEnggDAO_JDBC(dbconnection, dbname);

		return rdao;
	}
	
	public void deactivateConnection()
	{
		// Okay to keep deactivating an already deactivated connection
		activeConnection = false;
		if( dbconnection != null ){
			try{
				
				dbconnection.close();
				dbconnection = null;

			}
			catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}

}
