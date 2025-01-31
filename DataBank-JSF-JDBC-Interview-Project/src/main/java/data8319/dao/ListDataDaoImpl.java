/*********************************************************************************************************
 * File:  ListDataDaoImpl.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 */
package data8319.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import javax.sql.DataSource;

@SuppressWarnings("unused")
/**
 * Description:  API for reading list data from the database
 */
//TODO Annotate this class so that it becomes a managed bean
//TODO Provide the proper scope for this managed bean
@Named
@ApplicationScoped
public class ListDataDaoImpl implements ListDataDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	// Set the value of this string constant properly.  This is the JNDI name
	//     for the data source.
	private static final String DATA8319_DS_JNDI = "java:app/jdbc/data8319";
	// Set the value of this string constant properly.  This is the SQL
	//     statement to retrieve the list of Specialties from the database.
	private static final String READ_ALL_SPECIALTIES = 
			 "SELECT DISTINCT name FROM specialty;";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	// Use the proper annotation here so that the correct data source object
	//     will be injected
	@Resource(lookup=DATA8319_DS_JNDI)
	protected DataSource DATA8319DS;

	protected Connection conn;
	protected PreparedStatement readAllSpecialtiesPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg("building connection and stmts");
			conn = DATA8319DS.getConnection();
			// Initialize PreparedStatement here
			readAllSpecialtiesPstmt = conn.prepareStatement(READ_ALL_SPECIALTIES);
            
		} catch (Exception e) {
			logMsg("something went wrong getting connection from database:  " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg("closing stmts and connection");
			//Close PreparedStatement here
			readAllSpecialtiesPstmt.close();
			conn.close();
		} catch (Exception e) {
			logMsg("something went wrong closing stmts or connection:  " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<String> readAllSpecialties() {
		logMsg("reading all Specialties");
		List<String> specialties = new ArrayList<>();
		//Complete the retrieval of all Specialties 
		 try (ResultSet rs = readAllSpecialtiesPstmt.executeQuery()){
	            while (rs.next()) {
	               
	            	specialties.add(rs.getString("name"));
	            }
	            try {
	                rs.close();
	            }
	            catch (SQLException e) {
	                logMsg("something went wrong closing resultSet: " + e.getLocalizedMessage());
	            }
	        }
	        catch (SQLException e) {
	            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
	        }
		return specialties;

	}

}
