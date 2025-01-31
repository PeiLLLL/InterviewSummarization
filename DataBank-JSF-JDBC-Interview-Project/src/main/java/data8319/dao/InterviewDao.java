/*********************************************************************************************************
 * File:  InterviewDao.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 * @author (original) Mike Norman
 */
package data8319.dao;

import java.util.List;

import data8319.model.InterviewPojo;

/**
 * Description:  API for the database C-R-U-D operations
 */
public interface InterviewDao {

	// C
	public InterviewPojo createInterview(InterviewPojo Interview);

	// R
	public InterviewPojo readInterviewById(int InterviewId);

	public List<InterviewPojo> readAllInterviews();

	// U
	public void updateInterview(InterviewPojo Interview);

	// D
	public void deleteInterviewById(int InterviewId);

}