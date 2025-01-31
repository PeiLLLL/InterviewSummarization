/*********************************************************************************************************
 * File:  InterviewDaoImpl.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 * @author (original) Mike Norman
 */
package data8319.dao;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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


import data8319.model.InterviewPojo;

@SuppressWarnings("unused")
/**
 * Description:  Implements the C-R-U-D API for the database
 */
//managed bean with an application scope
@Named
@ApplicationScoped
public class InterviewDaoImpl implements InterviewDao, Serializable {
	/** Explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;
	// This is the JNDI name for the data source.
	private static final String DATA8319_DS_JNDI = "java:app/jdbc/data8319";
	//TThis is the SQL statement to retrieve the list of physicians from the database.
	private static final String READ_ALL = 
    		"SELECT * FROM interview";
	//This is the SQL statement to retrieve a physician by ID from the database.
	private static final String READ_INTERVIEW_BY_ID = 
			"SELECT * FROM interview WHERE id = ?";
	//This is the SQL statement to insert a new physician to the database.
	private static final String INSERT_INTERVIEW = 
    		"INSERT INTO interview (last_name, first_name, email, phone,specialty,content,summary,created) VALUES (?, ?, ?, ?,?, ?, ?,now());";
	//This is the SQL statement to update the fields of a physician in the database.
	private static final String UPDATE_INTERVIEW_ALL_FIELDS = 
			"UPDATE interview SET last_name = ?, first_name = ?, email = ?, phone = ?, specialty = ?, content = ?, summary = ? WHERE id = ?;";
	//This is the SQL statement to delete a physician from the database.
	private static final String DELETE_INTERVIEW_BY_ID = 
    		"DELETE FROM interview WHERE id = ?;";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg(String msg) {
		((ServletContext) externalContext.getContext()).log(msg);
	}

	//data source object will be injected here
    @Resource(lookup = DATA8319_DS_JNDI)
	protected DataSource DATA8319DS;

	protected Connection conn;
	protected PreparedStatement readAllPstmt;
	protected PreparedStatement readByIdPstmt;
	protected PreparedStatement createPstmt;
	protected PreparedStatement updatePstmt;
	protected PreparedStatement deleteByIdPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg("building connection and stmts");
			conn = DATA8319DS.getConnection();
			readAllPstmt = conn.prepareStatement(READ_ALL);
			createPstmt = conn.prepareStatement(INSERT_INTERVIEW, RETURN_GENERATED_KEYS);
			//Initialize other PreparedStatements 
			readByIdPstmt = conn.prepareStatement(READ_INTERVIEW_BY_ID);
			updatePstmt = conn.prepareStatement(UPDATE_INTERVIEW_ALL_FIELDS);
			deleteByIdPstmt = conn.prepareStatement(DELETE_INTERVIEW_BY_ID);
		} catch (Exception e) {
			logMsg("something went wrong getting connection from database:  " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg("closing stmts and connection");
			readAllPstmt.close();
			createPstmt.close();
			//Close other PreparedStatements
			readByIdPstmt.close();
			updatePstmt.close();
			deleteByIdPstmt.close();
			conn.close();
		} catch (Exception e) {
			logMsg("something went wrong closing stmts or connection:  " + e.getLocalizedMessage());
		}
	}

	@Override
	public List<InterviewPojo> readAllInterviews() {
		logMsg("reading all interview");
		List<InterviewPojo> interviews = new ArrayList<>();
		try (ResultSet rs = readAllPstmt.executeQuery();) {

			while (rs.next()) {
				InterviewPojo newInterview = new InterviewPojo();
				newInterview.setId(rs.getInt("id"));
				newInterview.setLastName(rs.getString("last_name"));
				//physician initialization
				newInterview.setFirstName(rs.getString("first_name"));
				newInterview.setEmail(rs.getString("email"));
				newInterview.setPhoneNumber(rs.getString("phone"));
				newInterview.setSpecialty(rs.getString("specialty"));
				newInterview.setContent(rs.getString("content"));
				newInterview.setSummary(rs.getString("summary"));

				Timestamp timestamp = rs.getTimestamp("created");
		            if (timestamp != null) {
		            	newInterview.setCreated(timestamp.toLocalDateTime());
		            }
				interviews.add(newInterview);
			}
			
		} catch (SQLException e) {
			logMsg("something went wrong accessing database:  " + e.getLocalizedMessage());
		}
		
		return interviews;

	}

	@Override
	public InterviewPojo createInterview(InterviewPojo interview) {
		logMsg("creating a physician");
		//insertion of a new physician
		try {
			createPstmt.setString(1, interview.getLastName());
			createPstmt.setString(2, interview.getFirstName());
			createPstmt.setString(3, interview.getEmail());
			createPstmt.setString(4, interview.getPhoneNumber());
			createPstmt.setString(5, interview.getSpecialty());
			createPstmt.setString(6, interview.getContent());
			createPstmt.setString(7, interview.getSummary());

			int affectedRows = createPstmt.executeUpdate();
	        logMsg("Affected rows: " + affectedRows);
	        if (affectedRows > 0) {
		        try (ResultSet generatedKeys = createPstmt.getGeneratedKeys()) {
		                if (generatedKeys.next()) {
		                	interview.setId(generatedKeys.getInt(1)); // Set generated ID
		                }
		            }
	        } 
			} catch (SQLException e) {
	            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
			}

		return interview;
	}

	@Override
	public InterviewPojo readInterviewById(int interviewId) {
		logMsg("read a specific physician");
		//retrieval of a specific physician by its id
        InterviewPojo interview = null;
		try {
    		readByIdPstmt.setLong(1, interviewId);
    		ResultSet rs = readByIdPstmt.executeQuery();
    		if (rs.next()) {
    			interview = new InterviewPojo();
    			interview.setId(rs.getInt("id"));
    			interview.setLastName(rs.getString("last_name"));
    			interview.setFirstName(rs.getString("first_name"));
    			interview.setEmail(rs.getString("email"));
    			interview.setPhoneNumber(rs.getString("phone"));
    			interview.setSpecialty(rs.getString("specialty"));
    			interview.setContent(rs.getString("content"));
    			interview.setSummary(rs.getString("summary"));

    		}
    	}
    	catch (SQLException e) {
               logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
        }
		return interview;
	}

	@Override
	public void updateInterview(InterviewPojo interview) {
		logMsg("updating a specific physician");
		//update of a specific interview here
		try {
	
			updatePstmt.setString(1, interview.getLastName());
			updatePstmt.setString(2, interview.getFirstName());
			updatePstmt.setString(3, interview.getEmail());
			updatePstmt.setString(4, interview.getPhoneNumber());
			updatePstmt.setString(5, interview.getSpecialty());
			updatePstmt.setString(6, interview.getContent());
			updatePstmt.setString(7, interview.getSummary()); 
			updatePstmt.setInt(8, interview.getId());

			updatePstmt.execute();
		} catch (SQLException e) {
			logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
		}
		
	}

	@Override
	public void deleteInterviewById(int interviewId) {
		logMsg("deleting a specific interview");
		//deletion of a specific physician 
		try {
    		deleteByIdPstmt.setLong(1, interviewId);
    		deleteByIdPstmt.execute();
    	}
    	catch (SQLException e) {
            logMsg("something went wrong accessing database: " + e.getLocalizedMessage());
    	}
	}

}