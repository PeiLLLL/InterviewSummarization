/*********************************************************************************************************
 * File:  ListDataDao.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 */
package data8319.dao;

import java.util.List;

/**
 * Description:  API for reading list data from the database
 */
public interface ListDataDao {

	public List<String> readAllSpecialties();

}