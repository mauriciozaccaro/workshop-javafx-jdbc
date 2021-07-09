package bd;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.protocol.Resultset;

public class DB {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties(); // carrega em "props" as propriedades do arquivo "db.properties"
				String url = props.getProperty("dburl"); //busca o que está na linha dburl do arquivo "db.properties"
				conn = DriverManager.getConnection(url, props);
			}
			catch(SQLException e ) {
				throw new DbException("Ocorreu um erro ao conectar o Banco de Dados: " + e.getMessage());
			}
		}
		return conn;
	}
	
	private static Properties loadProperties() {
		
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);//está passando as propriedades do arquivo "db.properties" para o Objeto Properties
			return props;
		}
		catch(IOException e ) {
			throw new DbException("Ocorreu um erro: " + e.getMessage());
		}
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	

	public static void closeResultSet(ResultSet rt) {
		if(rt != null) {
			try {
				rt.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
