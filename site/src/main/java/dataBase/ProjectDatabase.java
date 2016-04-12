/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package dataBase;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import models.Project;

/**
 * Projects database operations
 * @author shathe
 *
 */
@Component
public class ProjectDatabase {

	JdbcTemplate jdbcTemplate;

	public ProjectDatabase(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Lists the projects of a user
	 * @param idUser
	 * @return
	 */
	public List<Project> getProjects(String idUser) {
		return this.jdbcTemplate.query("select * from projectCrawlers where idUser = " + idUser, new ProjectMapper());
	}
	
	/**
	 * Deletes a project
	 * @param project
	 * @return
	 */
	public int deleteProject(Project project) {
		return this.jdbcTemplate.update("delete from  projectCrawlers where id = ?", project.getId());
	}

	/**
	 * Updates a project
	 * @param project
	 * @return
	 */
	public int updateProject(Project project) {
		return this.jdbcTemplate.update(// idUser,name, dslPath,pluginsPath,date
				"update projectCrawlers set name = ?,  dslPath = ?, pluginsPath = ? where id = ?", project.getName(),
				project.getDslPath(), project.getPluginsPath(), project.getId());
	}

	/**
	 * Creates a new project
	 * @param project
	 * @return
	 */
	public int createProject(Project project) {
		return this.jdbcTemplate.update(
				"insert into projectCrawlers (idUser,name, dslPath,pluginsPath,date) values (?,?,?,?,?)", project.getIdUser(),
				project.getName(), project.getDslPath(), project.getPluginsPath(),
				project.getDate());

	}
}
